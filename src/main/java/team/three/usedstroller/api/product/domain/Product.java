package team.three.usedstroller.api.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import team.three.usedstroller.api.common.domain.BaseTimeEntity;
import team.three.usedstroller.api.product.dto.OptionDto;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
@Builder
@Setter
@AllArgsConstructor
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Enumerated(EnumType.STRING)
	private SourceType sourceType;
	private String pid;
	@Column(length = 1000, nullable = false)
	private String title;
	private Long price;
	@Column(columnDefinition = "text")
	private String link;
	@Column(columnDefinition = "text")
	private String imgSrc;

	//naver
	private int releaseYear;
	@Column(length = 1000)
	private String etc;
	private LocalDate uploadDate;

	//bunjang
	private String address;

	//carrot
	private String region;

	@Column(columnDefinition = "text")
	private String content;

	@ManyToOne
	@JoinColumn(name = "model_id")
	private Model model;

	private String buyStatus;
	private int usePeriod;
	private int orderSeq;
}
