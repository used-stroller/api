package team.three.usedstroller.api.users.dto;

import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class AccountContext implements UserDetails {

  private final AccountDto accountDto;
  private final Set<GrantedAuthority> roles;

  public AccountContext(AccountDto accountDto, Set<GrantedAuthority> roles) {
    this.accountDto = accountDto;
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String getPassword() {
    return accountDto.getPassword();
  }

  @Override
  public String getUsername() {
    return accountDto.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
