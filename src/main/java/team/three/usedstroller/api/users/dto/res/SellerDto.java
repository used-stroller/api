package team.three.usedstroller.api.users.dto.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import team.three.usedstroller.api.product.domain.Product;

@Getter
@ToString
@Builder
public class SellerDto {
	private Long accountId;
	private String name;
	private String image;
	private String kakaoId;
}
