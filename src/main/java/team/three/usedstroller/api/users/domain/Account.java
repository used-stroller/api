package team.three.usedstroller.api.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;
import team.three.usedstroller.api.users.dto.Authority;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private String password;
  private String nickname;
  private String address;
  @Enumerated(EnumType.STRING)
  private Authority role;

  @Builder
  public Account(String email, String password, String nickname, String address, Authority role) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.address = address;
    this.role = role;
  }

  public Account(String email, String password) {
    this.email = email;
    this.password = password;
    this.nickname = "";
    this.address = "";
    this.role = Authority.ROLE_USER;
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateAddress(String address) {
    this.address = address;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void changeNickName(String nickname) {
    this.nickname = nickname;
  }

  public void changeAddress(String address) {
    this.address = address;
  }
}
