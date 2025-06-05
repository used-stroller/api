package team.three.usedstroller.api.users.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import team.three.usedstroller.api.product.domain.Product;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyPageDto {
	private Long accountId;
	private String name;
	private String image;
	private String kakaoId;
}
