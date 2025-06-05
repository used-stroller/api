package team.three.usedstroller.api.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.product.domain.Model;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ModelDto {

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
	private String imageUrl;
	private Integer weightScore;
	private Integer brandScore;
	private Integer driveScore;
	private Integer safeScore;
	private Integer priceScore;
	private Integer flightScore;


	// Entity → DTO
	public static ModelDto of(Model entity) {
		return ModelDto.builder()
			.id(entity.getId())
			.launched(entity.getLaunched())
			.country(entity.getCountry())
			.brand(entity.getBrand())
			.name(entity.getName())
			.minAge(entity.getMinAge())
			.maxAge(entity.getMaxAge())
			.foldingType(entity.getFoldingType())
			.newBornAvailable(entity.getNewBornAvailable())
			.newPrice(entity.getNewPrice())
			.usedPrice(entity.getUsedPrice())
			.recommendPrice(entity.getRecommendPrice())
			.weight(entity.getWeight())
			.weightType(entity.getWeightType())
			.strollerType(entity.getStrollerType())
			.size(entity.getSize())
			.reclining(entity.getReclining())
			.carryOn(entity.getCarryOn())
			.twin(entity.getTwin())
			.etc(entity.getEtc())
			.imageUrl(entity.getImageUrl())
			.safeScore(entity.getSafeScore())
			.driveScore(entity.getDriveScore())
			.brandScore(entity.getBrandScore())
			.priceScore(entity.getPriceScore())
			.weightScore(entity.getWeightScore())
			.flightScore(entity.getFlightScore())
			.build();
	}
}
