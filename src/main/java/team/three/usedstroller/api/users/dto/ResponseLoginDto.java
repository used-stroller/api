package team.three.usedstroller.api.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseLoginDto {

  private ResponseLoginTokenDto responseLoginToken;
  private String kakaoId;
  private String name;
  private String image;


}
