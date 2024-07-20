package team.three.usedstroller.api.security.utils;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team.three.usedstroller.api.users.dto.Authority;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.access-expiration}")
  private Long expiration;

  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  private SecretKey secretKey;

  public static final String AUTHORIZATION = "Authorization";
  public static final String REFRESH = "refresh";
  public static final String ACCESS = "access";
  public static final String TYPE = "type";
  public static final String ROLE = "role";

  @PostConstruct
  public void init() {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(String email, Authority role) {
    return makeToken(ACCESS, email, role, expiration);
  }

  public String generateRefreshToken(String email, Authority role) {
    return makeToken(REFRESH, email, role, refreshExpiration);
  }

  private String makeToken(String type, String email, Authority role, Long expiration) {
    return Jwts.builder()
        .subject(email)
        .claim(TYPE, type)
        .claim(ROLE, role.name())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(secretKey)
        .compact();
  }

  public boolean isExpired(String token) {
    try {
      return Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getExpiration()
          .before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return true;
    }
  }

  public String getEmailFromToken(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public String getType(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(TYPE, String.class);
  }

  public Date getExpiration(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration();
  }

  public String getRole(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(ROLE, String.class);
  }

}
