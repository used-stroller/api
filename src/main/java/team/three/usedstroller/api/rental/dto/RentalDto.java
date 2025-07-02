package team.three.usedstroller.api.rental.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import team.three.usedstroller.api.users.entity.Account;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentalDto {
	private Long id;
	private String code;
	private String productName;
	private String src;
	private String color;
	private Long rentalPrice;
	private boolean isRentable;
	private String productionDate;

}
