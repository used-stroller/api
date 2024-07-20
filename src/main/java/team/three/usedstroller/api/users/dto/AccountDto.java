package team.three.usedstroller.api.users.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import team.three.usedstroller.api.users.domain.Account;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountDto {

  private String email;
  private String password;
  private String nickname;
  private String address;

  public AccountDto(String email, String password) {
    this.email = email.trim();
    this.password = password.trim();
  }

  public AccountDto(String email, String password, String nickname, String address) {
    this.email = email.trim();
    this.password = password.trim();
    this.nickname = nickname.trim();
    this.address = address.trim();
  }

  public static AccountDto of(Account user) {
    return new AccountDto(user.getEmail(), user.getPassword(), user.getNickname(), user.getAddress());
  }

  public boolean isValid() {
    return StringUtils.hasText(email) && StringUtils.hasText(password);
  }
}
