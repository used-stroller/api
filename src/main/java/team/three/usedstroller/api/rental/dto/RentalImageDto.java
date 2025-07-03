package team.three.usedstroller.api.rental.dto;

import java.util.List;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.three.usedstroller.api.rental.entity.RentalEntity;
import team.three.usedstroller.api.rental.entity.RentalImageEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalImageDto {
	private Long id;
	private String src;
	private Long rentalId;
  	private int orderSeq;

  	public static RentalImageDto from(RentalImageEntity entity) {
        return new RentalImageDto(
            entity.getId(),
            entity.getSrc(),
            entity.getRental().getId(),
            entity.getOrderSeq()
        );
    }
}
