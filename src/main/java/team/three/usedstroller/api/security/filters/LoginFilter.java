package team.three.usedstroller.api.security.filters;

import static team.three.usedstroller.api.security.utils.JwtUtil.AUTHORIZATION;
import static team.three.usedstroller.api.security.utils.JwtUtil.REFRESH;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import team.three.usedstroller.api.security.entity.RefreshToken;
import team.three.usedstroller.api.security.repository.RefreshTokenRepository;
import team.three.usedstroller.api.security.utils.JwtUtil;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.Authority;
import team.three.usedstroller.api.users.dto.ResultDto;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
      RefreshTokenRepository refreshTokenRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    if (!HttpMethod.POST.name().equals(request.getMethod()) || !request.getRequestURI().startsWith("/login")) {
      throw new IllegalArgumentException("Authentication method not supported: " + request.getMethod());
    }

    AccountDto accountDto = null;
    try {
      accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }

    if (!accountDto.isValid()) {
      throw new AuthenticationServiceException("Email or password not provided");
    }

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        accountDto.getEmail(), accountDto.getPassword());
    return authenticationManager.authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    // 유저 정보
    String email = authResult.getName();

    GrantedAuthority auth = authResult.getAuthorities().iterator().next();
    Authority role = Authority.findByName(auth.getAuthority());

    // 토큰 생성
    String accessToken = jwtUtil.generateAccessToken(email, role);
    String refreshToken = jwtUtil.generateRefreshToken(email, role);

    //DB에 존재하는 해당 유저의 Refresh 토큰 모두 삭제
    deleteAllRefreshToken(email);
    //Refresh 토큰 DB에 저장
    saveRefreshToken(email, refreshToken);

    // 응답 설정
    response.setHeader(AUTHORIZATION, accessToken);
    response.addCookie(createCookie(REFRESH, refreshToken));
    response.setStatus(HttpStatus.OK.value());
  }

  private void deleteAllRefreshToken(String email) {
    refreshTokenRepository.deleteAllByEmail(email);
  }

  private void saveRefreshToken(String email, String token) {

    Date expiration = jwtUtil.getExpiration(token);
    RefreshToken refreshToken = new RefreshToken(email, token, expiration);
    refreshTokenRepository.save(refreshToken);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.addCookie(createCookie(REFRESH, null));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ResultDto resultDto = ResultDto.of(HttpStatus.UNAUTHORIZED, false,
        "Incorrect username or password");
    String jsonResponse = objectMapper.writeValueAsString(resultDto);

    PrintWriter writer = response.getWriter();
    writer.print(jsonResponse);
    writer.flush();
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    cookie.setHttpOnly(true);
    return cookie;
  }
}
