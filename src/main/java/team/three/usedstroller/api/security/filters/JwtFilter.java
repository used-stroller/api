package team.three.usedstroller.api.security.filters;

import static team.three.usedstroller.api.security.utils.JwtUtil.ACCESS;
import static team.three.usedstroller.api.security.utils.JwtUtil.AUTHORIZATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import team.three.usedstroller.api.security.utils.JwtUtil;
import team.three.usedstroller.api.users.dto.ResultDto;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = getToken(request);

    // 1. 토큰이 없다면 인증이 필요없는 요청도 있기 때문에 다음 필터로 넘김
    if (!StringUtils.hasText(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 2. 토큰이 있다면 만료 여부 확인. 만료시 다음 필터로 넘기지 않음
    String email = null;
    if (!jwtUtil.isExpired(token)) {
      email = jwtUtil.getEmailFromToken(token);
    } else {
      changeResponse(response, "Access token expired");
      return;
    }

    // access token 인지 확인
    if (!ACCESS.equals(jwtUtil.getType(token))) {
      changeResponse(response, "Invalid access token");
      return;
    }

    if (StringUtils.hasText(email)
        && SecurityContextHolder.getContext().getAuthentication() == null) {

      String role = jwtUtil.getRole(token);
      Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(role));
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          email, null, authorities);
      usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    filterChain.doFilter(request, response);
  }

  private void changeResponse(HttpServletResponse response, String message)
      throws IOException {
    ResultDto resultDto = ResultDto.of(HttpStatus.UNAUTHORIZED, false, message);
    String jsonResultDto = objectMapper.writeValueAsString(resultDto);

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    PrintWriter writer = response.getWriter();
    writer.print(jsonResultDto);
    writer.flush();
  }

  private String getToken(HttpServletRequest request) {

    final String token = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(token)) {
      return token;
    }
    return null;
  }
}
