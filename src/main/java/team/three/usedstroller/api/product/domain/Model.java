package team.three.usedstroller.api.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "model")
public class Model extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  private String name;
  private String brand;
  private String price;
  private Long recommendPrice;


  @Builder
  private Model(Long id, String name, String brand,String price,Long recommendPrice) {
    this.name = name;
    this.brand = brand;
    this.price = price;
    this.recommendPrice = recommendPrice;
  }
}
