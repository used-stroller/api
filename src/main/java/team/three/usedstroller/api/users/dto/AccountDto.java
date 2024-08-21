package team.three.usedstroller.api.users.dto;

import lombok.AccessLevel;
import lombok.Builder;
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

  @Builder
  public AccountDto(String email, String password, String nickname, String address) {
    this.email = email == null ? null : email.trim();
    this.password = password == null ? null : password.trim();
    this.nickname = nickname == null ? null : nickname.trim();
    this.address = address == null ? null : address.trim();
  }

  public static AccountDto of(Account user) {
    return AccountDto.builder()
        .email(user.getEmail())
        .password(user.getPassword())
        .nickname(user.getNickname())
        .address(user.getAddress())
        .build();
  }

  public static AccountDto toDto(Account user) {
    return AccountDto.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .address(user.getAddress())
        .build();
  }

  public boolean isValid() {
    return StringUtils.hasText(email) && StringUtils.hasText(password);
  }
}
