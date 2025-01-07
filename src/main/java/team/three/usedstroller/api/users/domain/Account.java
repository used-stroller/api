package team.three.usedstroller.api.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;
import team.three.usedstroller.api.users.dto.Authority;

@Getter
@Entity
@Builder
@AllArgsConstructor
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
  private String kakaoId;
  private String name;
  private String image;
  @Enumerated(EnumType.STRING)
  private Authority role;

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
