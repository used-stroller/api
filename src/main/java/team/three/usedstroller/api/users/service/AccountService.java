package team.three.usedstroller.api.users.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import team.three.usedstroller.api.common.jwt.JwtTokenProvider;
import team.three.usedstroller.api.users.domain.Account;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.LoginWrapperDto;
import team.three.usedstroller.api.users.dto.LoginWrapperDto.LoginResultDto.UserDto;
import team.three.usedstroller.api.users.dto.ResponseLoginDto;
import team.three.usedstroller.api.users.dto.ResponseLoginTokenDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.repository.AccountRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public ResultDto createUser(AccountDto accountDto) {
    String email = accountDto.getEmail().trim();
    String password = accountDto.getPassword().trim();
    boolean exists = accountRepository.existsAccountByEmail(email);
    if (exists) {
      return ResultDto.of(HttpStatus.BAD_REQUEST, false, "이미 존재하는 이메일입니다.");
    }
    Account account = Account.builder().email(email).password(password).build();
    account.updatePassword(passwordEncoder.encode(password));
    Account saved = accountRepository.save(account);
    if (!ObjectUtils.isEmpty(saved)) {
      return ResultDto.of(HttpStatus.CREATED, true, "가입이 완료되었습니다.");
    }
    return ResultDto.of(HttpStatus.BAD_REQUEST,false, "회원가입에 실패했습니다.");
  }

  @Transactional(readOnly = true)
  public AccountDto getAccountByEmail(String email) {
    Account account = accountRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
    return AccountDto.toDto(account);
  }

  @Transactional
  public void updateAccount(String email, AccountDto accountDto) {
    Account account = accountRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(email));
    account.changeNickName(accountDto.getNickname());
    account.changeAddress(accountDto.getAddress());
  }

  public void loginOrSignUp(LoginWrapperDto loginResult, HttpServletResponse response) {
    // 기존회원 리다이렉트
    String kakaoId = loginResult.getLoginResult().getUser().getKakaoId();
    if(!accountRepository.existsAccountByKakaoId(kakaoId)) {
      saveNewUserInfo(loginResult, kakaoId);
    }

    Optional<Account> accountEntity = accountRepository.findByKakaoId(kakaoId);

    // 토큰 생성
    ResponseLoginTokenDto responseLoginTokenDto = jwtTokenProvider.generateTokenDto(accountEntity.get());

    Cookie cookie = new Cookie("jwt", responseLoginTokenDto.getAccessToken());
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60*60); // 1시간

    response.addCookie(cookie);

    return ResponseLoginDto.builder()

        .build();
  }

  private void saveNewUserInfo(LoginWrapperDto loginResult, String kakaoId) {
    Account newAccount = Account.builder()
        .kakaoId(kakaoId)
        .image(loginResult.getLoginResult().getUser().getImage())
        .name(loginResult.getLoginResult().getUser().getName())
        .email(kakaoId + loginResult.getLoginResult().getUser().getName())
        .password("")
        .build();
    accountRepository.save(newAccount);
  }
}
