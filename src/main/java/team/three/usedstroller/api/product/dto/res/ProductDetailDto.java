package team.three.usedstroller.api.product.dto.res;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import team.three.usedstroller.api.product.dto.ImageDto;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.dto.res.SellerDto;

@Data
@Builder
public class ProductDetailDto {
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String content;
  private Long price;
  private String region;
  private String title;
  private String buyStatus;
  private int usePeriod;
  private List<ImageDto> imageList;
  private List<Long> options;
  private boolean favorite;
  private MyPageDto myPageDto;
  private SellerDto seller;
  private Long sellerId;
}

