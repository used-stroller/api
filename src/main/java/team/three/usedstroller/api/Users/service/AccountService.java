package team.three.usedstroller.api.Users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.three.usedstroller.api.Users.domain.Account;
import team.three.usedstroller.api.Users.domain.dto.AccountDto;
import team.three.usedstroller.api.Users.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Account createUser(AccountDto accountDto) {
    boolean exists = accountRepository.existsAccountByEmail(accountDto.getEmail());
    if (exists) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }
    Account account = new Account(accountDto);
    account.updatePassword(passwordEncoder.encode(accountDto.getPassword()));
    return accountRepository.save(account);
  }
}
