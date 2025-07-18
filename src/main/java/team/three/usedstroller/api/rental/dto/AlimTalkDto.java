package team.three.usedstroller.api.rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlimTalkDto {
	private Long rentalId;
	private String name;
	private String phone;
	private String rentalPeriod;
	private String useDate;
}
