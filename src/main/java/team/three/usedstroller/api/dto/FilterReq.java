package team.three.usedstroller.api.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.three.usedstroller.api.domain.SourceType;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FilterReq {

  private String keyword;
  private SourceType sourceType;
  private Long minPrice;
  private Long maxPrice;
  private String town;
  private List<String> model;
  private String period;
  private List<String> brand;
}
