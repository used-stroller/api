package team.three.usedstroller.api.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseLoginTokenDto {
  private String grantType;

  private String accessToken;

  private String refreshToken;

  private Long accessTokenExpiresIn;
}
