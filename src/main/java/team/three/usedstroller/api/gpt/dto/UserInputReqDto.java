package team.three.usedstroller.api.gpt.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.enums.StrollerType;
import team.three.usedstroller.api.enums.WeightType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInputReqDto {
  private String ageCode;
  private Long maxPriceNew;
  private Long maxPriceUsed;
  private String twin;
  // private StrollerType type;
  // private WeightType weightType;
  private String userText;
  private List<Integer> weightKeywordList;
  private String sessionId;

  public Boolean getTwin() {
    return "yes".equalsIgnoreCase(twin);
  }
}
