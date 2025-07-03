package team.three.usedstroller.api.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "rental_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalImageEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String src;
  @ManyToOne(fetch = FetchType.LAZY)
  // 렌탈 상품 id
  @JoinColumn(name = "rental_id")
  private RentalEntity rental;
  private int orderSeq;
}
