package team.three.usedstroller.api.product.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDto {
  private Long id;
  private String title;
  private String region;
  private String link;
  private Long favoriteCnt;
  private boolean favorite;
}

