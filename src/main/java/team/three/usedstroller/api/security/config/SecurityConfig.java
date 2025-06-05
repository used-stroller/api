package team.three.usedstroller.api.security.config;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import team.three.usedstroller.api.common.jwt.JwtAccessDeniedHandler;
import team.three.usedstroller.api.common.jwt.JwtAuthenticationEntryPoint;
import team.three.usedstroller.api.common.jwt.JwtTokenProvider;
import team.three.usedstroller.api.security.filters.JwtAuthenticationFilter;
import team.three.usedstroller.api.security.repository.RefreshTokenRepository;

@Slf4j
@EnableScheduling
@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {

  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  /**
   * security 필터를 거치지 않는다. 보통 html이나 css, image 등 정적자원들을 주로 설정한다.
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return webSecurity -> webSecurity.ignoring().requestMatchers("/image/**");
  }

  @Bean
  public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .securityContext(context -> context.requireExplicitSave(true)) // SecurityContext 명시적 저장 설정
        .anonymous(AbstractHttpConfigurer::disable) // 익명 인증 비활성화, AnonymousAuthenticationFilter가 익명사용자로 덮어씌움 
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/api/product/list/**","/api/product/get/**","/login", "/api/user/signup", "/reissue","/api/auth/kakao/**","/api/backend/auth/kakao/**","/api/auth/callback/**","/api/gpt/**","/error").permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(exceptions -> exceptions
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Use your custom entry point
            .accessDeniedHandler(jwtAccessDeniedHandler)
        )

        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  // public CorsConfigurationSource corsConfigurationSource() {
  //   CorsConfiguration corsConfiguration = new CorsConfiguration();
  //   corsConfiguration.setAllowedOrigins(List.of(
  //       "http://localhost:3000",
  //       "https://jungmocha.co.kr",
  //       "https://front-git-feature-gpt-donghuns-projects.vercel.app"
  //
  //   ));
  //   corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
  //   corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
  //   corsConfiguration.setAllowCredentials(true);
  //   corsConfiguration.setMaxAge(Duration.ofDays(1));
  //
  //   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  //   source.registerCorsConfiguration("/**", corsConfiguration);
  //   return source;
  // }
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      String origin = request.getHeader("Origin");
      log.info("🔥 CORS 요청 origin: {}", origin); // 여기에 찍히는지 확인

      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedOrigins(List.of(
          "http://localhost:3000",
          "https://jungmocha.co.kr",
          "https://api.jungmocha.shop"
      ));
      config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
      config.setAllowedHeaders(List.of("*"));
      config.setAllowCredentials(true);
      config.setMaxAge(Duration.ofDays(1));

      return config;
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

 @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
 public void reissueToken() {
   refreshTokenRepository.deleteAllExpiredToken(new Date(System.currentTimeMillis()));
   log.info("만료된 RefreshToken 모두 삭제 완료");
 }
}
