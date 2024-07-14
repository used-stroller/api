package team.three.usedstroller.api.Users.domain.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.Users.domain.Account;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountDto {

  private String email;
  private String password;
  private String nickname;
  private String address;

  @Builder
  public AccountDto(String email, String password, String nickname, String address) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.address = address;
  }

  public static AccountDto of(Account user) {
    return AccountDto.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .address(user.getAddress())
        .build();
  }
}
