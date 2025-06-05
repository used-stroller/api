package team.three.usedstroller.api.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import team.three.usedstroller.api.common.jwt.EndPointConf;
import team.three.usedstroller.api.common.jwt.JwtTokenProvider;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final JwtTokenProvider jwtTokenProvider;
  private final PathMatcher pathMatcher = new AntPathMatcher();


  // 실제 필터링 로직은 doFilterInternal 에 들어감
  // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    // 인증 제외 endpoint 처리
    String requestURI = request.getRequestURI();
    boolean permitAll = false;
    for (String permitAllEndpoint : EndPointConf.NOT_JWT_AUTH_ENDPOINT_LIST) {
      if (pathMatcher.match(permitAllEndpoint, requestURI)) {
        permitAll = true;
        break;
      }
    }
    // 인증제외 endpoint permitall 되었어도 토큰 유효하면 setAuth 처리
    String jwt = resolveToken(request);
    log.info("jwt={}",jwt);

    if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("authentication 설정됨={}",authentication);
    }
    // permitAll 경우 인증 처리 없이 요청을 다음 필터로 넘기고 끝
    if (permitAll) {
      filterChain.doFilter(request, response);
      return;
    }

    log.info("jwt필터통과");
    // 나머지 경우 필터 체인 실행 (인증 필요 endpoint)
    filterChain.doFilter(request, response);
  }

  // Request Header 에서 토큰 정보를 꺼내오기
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("jwt")) {
          return cookie.getValue();
        }
      }
    }

    return null;
  }
}
