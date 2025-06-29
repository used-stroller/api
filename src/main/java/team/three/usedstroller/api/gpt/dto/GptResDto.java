package team.three.usedstroller.api.gpt.dto;

public class GptResDto {
  private Long age;
  private Long minPrice;
  private Long maxPrice;
  private StrollerType type;
  private WeightType weightType;


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
