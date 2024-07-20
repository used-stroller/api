package team.three.usedstroller.api.security.config;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.users.domain.Account;
import team.three.usedstroller.api.users.dto.AccountContext;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account = accountRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

    Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(account.getRole().name()));
    AccountDto accountDto = AccountDto.of(account);
    return new AccountContext(accountDto, authorities);
  }
}
