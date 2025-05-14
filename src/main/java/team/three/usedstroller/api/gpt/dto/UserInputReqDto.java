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
  private Long age;
  private Long maxPriceNew;
  private Long maxPriceUsed;
  private Boolean twin;
  private StrollerType type;
  private WeightType weightType;
  private Boolean carryOn;
  private String userText;
  private List<String> weightKeywordList;
}
