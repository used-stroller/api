package team.three.usedstroller.api.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import team.three.usedstroller.api.users.domain.Account;
import team.three.usedstroller.api.users.dto.ResponseLoginTokenDto;

@Slf4j
@Component
public class JwtTokenProvider {
  private static final String AUTHORITIES_KEY = "auth";
  public static final String MEMBER_ID  = "memberId";
  public static final String KAKAO_ID  = "IDX";
  public static final String MEMBER_NAME = "NAME";

  private static final String BEARER_TYPE = "Bearer";
  //private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;       // 1시간
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

  private final SecretKey key;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public ResponseLoginTokenDto generateTokenDto(Account account) {

    long now = (new Date()).getTime();

    // Access Token 생성
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
        .claim(MEMBER_ID     , account.getId())
        .claim(KAKAO_ID     , account.getKakaoId())
        .claim(MEMBER_NAME    , account.getName())
        .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
        .signWith(key, SignatureAlgorithm.HS512)    // header  "alg": "HS512"
        .compact();

    // Refresh Token 생성
    String refreshToken = Jwts.builder()
        .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();

    return ResponseLoginTokenDto.builder()
        .grantType(BEARER_TYPE)
        .accessToken(accessToken)
        .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
        .refreshToken(refreshToken)
        .build();
  }

  public Authentication getAuthentication(String accessToken) {
    // 토큰 복호화
    Claims claims = parseClaims(accessToken);

    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities = Stream.of(
            Arrays.stream(claims.get(MEMBER_ID).toString().split(","))
                .map(key -> new SimpleGrantedAuthority(MEMBER_ID + "=" + key)),
            Arrays.stream(claims.get(KAKAO_ID).toString().split(","))
                .map(key -> new SimpleGrantedAuthority(KAKAO_ID + "=" + key)),
            Arrays.stream(claims.get(MEMBER_NAME).toString().split(","))
                .map(key -> new SimpleGrantedAuthority(MEMBER_NAME + "=" + key))
        ).flatMap(stream -> stream)
        .collect(Collectors.toList());
    String memberId = authorities.stream().filter(auth -> auth.getAuthority().startsWith(MEMBER_ID + "="))
            .map(auth -> auth.getAuthority().substring(MEMBER_ID.length() +1))
                .findFirst().orElse("defalutUser");

    // UserDetails 객체를 만들어서 Authentication 리턴
    UserDetails principal = new User(memberId, "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰이 잘못되었습니다. : " + e.getMessage());
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public Map<String, Object> parseTokenData(String token) {
    Claims claims = parseClaims(token);
    if (claims == null) {
      return null;
    }
    Map<String, Object> tokenData = new HashMap<>();
    tokenData.put(MEMBER_ID,  claims.get(MEMBER_ID));
    tokenData.put(KAKAO_ID,  claims.get(KAKAO_ID));
    tokenData.put(MEMBER_NAME, claims.get(MEMBER_NAME));
    return tokenData;
  }
}
