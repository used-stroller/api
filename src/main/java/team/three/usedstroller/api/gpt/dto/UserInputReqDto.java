package team.three.usedstroller.api.gpt.dto;

import lombok.Getter;

@Getter
public class UserInputReqDto {
  private Long age;
  private Long minPrice;
  private Long maxPrice;
  private Boolean twin;
  private StrollerType type;
  private WeightType weightType;
  private Boolean carryOn;
  private String userText;


  public enum WeightType {
    HEAVY("무거움"),
    NORMAL("보통"),
    LIGHT("가벼움");

    private String weight;

    WeightType(String weight) { this.weight = weight;}

  }

  public enum StrollerType {
    DELUXE("디럭스"),
    CONVERTIBLE("절충형"),
    HANDY("휴대용");

    private String type;

    StrollerType(String type) { this.type = type;}

  }
}
