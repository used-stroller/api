package team.three.usedstroller.api.users.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import team.three.usedstroller.api.security.utils.JwtUtil;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.Authority;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.repository.AccountRepository;
import team.three.usedstroller.api.users.service.AccountService;
import team.three.usedstroller.api.util.WebIntegrationTest;

class UsersControllerTest extends WebIntegrationTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  JwtUtil jwtUtil;

  @BeforeEach
  void init() {
    accountRepository.deleteAll();
  }

  @Test
  void create_user() {
    //given
    AccountDto accountDto = AccountDto.builder()
        .email("test")
        .password("1111")
        .build();

    //when
    ResultDto user = accountService.createUser(accountDto);

    //then
    assertThat(user.getStatus()).isEqualTo(HttpStatus.CREATED);
    assertThat(user.isSuccess()).isTrue();
    assertThat(user.getMessage()).isEqualTo("가입이 완료되었습니다.");
  }

  @DisplayName("30분 유효시간의 access token을 만들 수 있다")
  @Test
  void create_access_token() {
    //given
    String email = "test@test.com";
    String type = "access";
    int expiration = 1800000; //30분
    String accessToken = jwtUtil.generateAccessToken(email, Authority.ROLE_USER);

    //when
    String findEmail = jwtUtil.getEmailFromToken(accessToken);
    String findType = jwtUtil.getType(accessToken);
    Date expirationDate1 = jwtUtil.getExpiration(accessToken);

    //then
    Date expirationDate2 = new Date(System.currentTimeMillis() + expiration);
    assertThat(accessToken).isNotNull();
    assertThat(findEmail).isEqualTo(email);
    assertThat(findType).isEqualTo(type);
    assertThat(expirationDate1).isBeforeOrEqualTo(expirationDate2);
    System.out.println("accessToken = " + accessToken);
    System.out.println("expirationDate1 = " + expirationDate1);
    System.out.println("expirationDate2 = " + expirationDate2);
  }

  @DisplayName("7일분 유효시간의 refresh token을 만들 수 있다")
  @Test
  void create_refresh_token() {
    //given
    String email = "test@test.com";
    String type = "refresh";
    int expiration = 604800000; //7일
    String refreshToken = jwtUtil.generateRefreshToken(email, Authority.ROLE_USER);

    //when
    String findEmail = jwtUtil.getEmailFromToken(refreshToken);
    String findType = jwtUtil.getType(refreshToken);
    Date expirationDate1 = jwtUtil.getExpiration(refreshToken);

    //then
    Date expirationDate2 = new Date(System.currentTimeMillis() + expiration);
    assertThat(refreshToken).isNotNull();
    assertThat(findEmail).isEqualTo(email);
    assertThat(findType).isEqualTo(type);
    assertThat(expirationDate1).isBeforeOrEqualTo(expirationDate2);
    System.out.println("refreshToken = " + refreshToken);
    System.out.println("expirationDate1 = " + expirationDate1);
    System.out.println("expirationDate2 = " + expirationDate2);
  }
}