package team.three.usedstroller.api.product.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import team.three.usedstroller.api.product.domain.SourceType;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FilterReq {

  private String keyword;
  private List<SourceType> sourceType;
  private Long minPrice;
  private Long maxPrice;
  private String fixedAddress;
  private String detailAddress;
  private String region;
  private Integer period;
  private List<String> model;
  private List<String> brand;
  private String recommendYn;
}
