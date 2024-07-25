package team.three.usedstroller.api.security.controller;

import static team.three.usedstroller.api.security.utils.JwtUtil.AUTHORIZATION;
import static team.three.usedstroller.api.security.utils.JwtUtil.REFRESH;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.security.entity.RefreshToken;
import team.three.usedstroller.api.security.repository.RefreshTokenRepository;
import team.three.usedstroller.api.security.utils.JwtUtil;
import team.three.usedstroller.api.users.dto.Authority;
import team.three.usedstroller.api.users.dto.ResultDto;

@RestController
@RequiredArgsConstructor
public class ReissueController {

  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  @PostMapping("/reissue")
  public ResponseEntity<ResultDto> reissue(HttpServletRequest request, HttpServletResponse response) {
    String oldRefreshToken = extractTokenFromCookies(request.getCookies());

    if (!StringUtils.hasText(oldRefreshToken)) {
      return buildErrorResponse("Refresh token is null");
    }

    if (jwtUtil.isExpired(oldRefreshToken) ||
        !REFRESH.equals(jwtUtil.getType(oldRefreshToken)) ||
        Boolean.FALSE.equals(refreshTokenRepository.existsByToken(oldRefreshToken))
    ) {
      return buildErrorResponse("Invalid refresh token");
    }

    String email = jwtUtil.getEmailFromToken(oldRefreshToken);
    Authority role = Authority.findByName(jwtUtil.getRole(oldRefreshToken));

    String newAccessToken = jwtUtil.generateAccessToken(email, role);
    String newRefreshToken = jwtUtil.generateRefreshToken(email, role);

    refreshTokenRepository.deleteByToken(oldRefreshToken);
    saveRefreshToken(email, newRefreshToken);

    response.setHeader(AUTHORIZATION, newAccessToken);
    response.addCookie(createCookie(REFRESH, newRefreshToken));

    return buildSuccessResponse("Token refresh successfully");
  }

  private String extractTokenFromCookies(Cookie[] cookies) {
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (REFRESH.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  private void saveRefreshToken(String email, String token) {

    Date expiration = jwtUtil.getExpiration(token);
    RefreshToken refreshToken = new RefreshToken(email, token, expiration);
    refreshTokenRepository.save(refreshToken);
  }

  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24*60*60);
    cookie.setHttpOnly(true);
    return cookie;
  }

  private ResponseEntity<ResultDto> buildErrorResponse(String message) {
    ResultDto resultDto = ResultDto.of(HttpStatus.BAD_REQUEST, false, message);
    return ResponseEntity.badRequest().body(resultDto);
  }

  private ResponseEntity<ResultDto> buildSuccessResponse(String message) {
    ResultDto resultDto = ResultDto.of(HttpStatus.CREATED, true, message);
    return ResponseEntity.ok(resultDto);
  }
}
