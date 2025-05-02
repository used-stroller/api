package team.three.usedstroller.api.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "model")
public class Model extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  private String launched;
  private String country;
  private String brand;
  private String name;
  private Long minAge;
  private Long maxAge;
  private String foldingType;
  private Boolean newBornAvailable;
  private Long newPrice;
  private Long usedPrice;
  private Long recommendPrice;
  private float weight;
  private String weightType;
  private String strollerType;
  private String size;
  private String reclining;
  private Boolean carryOn;
  private Boolean twin;
  private String etc;
  private String ImageUrl;

}
