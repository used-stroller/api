package team.three.usedstroller.api.rental.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class RentalEntity extends BaseTimeEntity {

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

  @OneToMany(mappedBy = "rental")
  List<RentalImageEntity> images = new ArrayList<>();
}
