package team.three.usedstroller.api.gpt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Setter;
import lombok.ToString;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_summary")
public class ReviewSummaryEntity extends BaseTimeEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "summary")
  private String summary;

  @ManyToOne
  @JoinColumn(name = "review_id")
  private BlogReviewEntity review;

  @Column(name = "weight")
  String weight;

  @Column(name = "max_weight")
  String maxWeight;

  @Column(name = "stroller_type")
  String strollerType;

  @Column(name = "one_hand_folding")
  String oneHandFolding;

  @Column(name = "easy_folding")
  String easyFolding;

  @Column(name = "age")
  String age;

  @Column(name = "size")
  String size;

  @Column(name = "adjust_handle")
  Boolean adjustHandle;

  @Column(name = "reclining")
  String reclining;

  @Column(name = "reversible_seat")
  Boolean reversibleSeat;

  @Column(name = "price")
  String price;

  @Column(name = "carry_on")
  Boolean carryOn;

  @Column(name = "model_id")
  Long modelId;

}
