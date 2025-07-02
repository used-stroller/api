package team.three.usedstroller.api.rental.entity;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;
import team.three.usedstroller.api.product.domain.Model;
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
  private String code;
  private String productName;
  private String src;
  private String color;
  private String grade;
  private Long rentalPrice;
  private boolean isRentable;
  private String productionDate; //제조일
  private String description; // 설명

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="model_id")
  private Model model;

  @OneToMany(mappedBy = "rental")
  List<RentalImageEntity> images = new ArrayList<>();
}
