package team.three.usedstroller.api.product.dto.req;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeStatusReq implements Serializable {

  private Long id;
  private String statusType;
}
