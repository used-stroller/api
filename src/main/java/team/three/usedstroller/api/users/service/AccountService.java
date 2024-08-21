package team.three.usedstroller.api.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import team.three.usedstroller.api.users.domain.Account;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ResultDto createUser(AccountDto accountDto) {
    String email = accountDto.getEmail().trim();
    String password = accountDto.getPassword().trim();
    boolean exists = accountRepository.existsAccountByEmail(email);
    if (exists) {
      return ResultDto.of(HttpStatus.BAD_REQUEST, false, "이미 존재하는 이메일입니다.");
    }
    Account account = new Account(email, password);
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
}
