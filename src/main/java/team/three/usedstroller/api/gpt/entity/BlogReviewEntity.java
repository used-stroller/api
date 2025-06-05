package team.three.usedstroller.api.gpt.entity;

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
@Table(name = "blog_review")
public class BlogReviewEntity extends BaseTimeEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "brand")
  private String brand;

  @Column(name = "model_name")
  private String modelName;

  @Column(name = "source_title")
  private String sourceTitle;

  @Column(name = "source_url")
  private String sourceUrl;

  @Column(name = "original_content")
  private String originalContent;

  @Column(name = "summary")
  private String summary;

  @Column(name = "is_summarized")
  private boolean summarized;

}
