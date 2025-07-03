package team.three.usedstroller.api.rental.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import lombok.Setter;
import team.three.usedstroller.api.rental.entity.RentalEntity;
import team.three.usedstroller.api.users.entity.Account;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalDto {
	private Long id;
	private String code;
	private String productName;
	private String src;
	private String color;
	private Long rentalPrice;
	private boolean isRentable;
	private String productionDate;
	private String description;
	private String descriptionImage;
	private List<RentalImageDto> rentalImages;

	    // 정적 팩토리 메서드 추가
    public static RentalDto from(RentalEntity entity) {
        List<RentalImageDto> imageDtos = entity.getImages().stream()
            .map(RentalImageDto::from)
            .collect(Collectors.toList());

        return new RentalDto(
            entity.getId(),
            entity.getCode(),
            entity.getProductName(),
            entity.getSrc(),
            entity.getColor(),
			entity.getRentalPrice(),
			entity.isRentable(),
			entity.getProductionDate(),
            entity.getDescription(),
            entity.getDescriptionImage(),
            imageDtos
        );
    }
}
