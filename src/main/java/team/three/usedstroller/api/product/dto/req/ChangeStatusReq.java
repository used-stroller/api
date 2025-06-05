package team.three.usedstroller.api.product.dto.req;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.three.usedstroller.api.enums.RegisterType;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeStatusReq implements Serializable {

  private Long productId;
  private RegisterType statusType;
}
