package team.three.usedstroller.api.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import team.three.usedstroller.api.security.repository.RefreshTokenRepository;
import team.three.usedstroller.api.security.utils.JwtUtil;

public class CustomLogoutFilter extends GenericFilterBean {

  private static final String LOGOUT_URI = "/logout";
  private static final String REFRESH_COOKIE_NAME = "refresh";

  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  public CustomLogoutFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
    this.jwtUtil = jwtUtil;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (isLogoutRequest(request)) {
      handleLogout(request, response);
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private boolean isLogoutRequest(HttpServletRequest request) {
    return LOGOUT_URI.equals(request.getRequestURI()) &&
        HttpMethod.POST.name().equals(request.getMethod());
  }

  private void handleLogout(HttpServletRequest request, HttpServletResponse response) {
    Optional<String> refreshToken = getRefreshToken(request);

    if (refreshToken.isEmpty() || !isValidToken(refreshToken.get())) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    //로그아웃 진행
    refreshTokenRepository.deleteByToken(refreshToken.get());
    removeRefreshTokenCookie(response);
    response.setStatus(HttpServletResponse.SC_OK);
  }

  private void removeRefreshTokenCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie(REFRESH_COOKIE_NAME, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
  }

  private boolean isValidToken(String token) {
    return StringUtils.hasText(token) &&
        !jwtUtil.isExpired(token) &&
        REFRESH_COOKIE_NAME.equals(jwtUtil.getType(token)) &&
        refreshTokenRepository.existsByToken(token);
  }

  private Optional<String> getRefreshToken(HttpServletRequest request) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> REFRESH_COOKIE_NAME.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst();
  }
}
