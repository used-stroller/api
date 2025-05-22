package team.three.usedstroller.api.product.dto.res;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.dto.ImageDto;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.dto.res.SellerDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  private Long id;
  private String sourceType; // Enum → String
  private String pid;
  private String title;
  private Long price;
  private String link;
  private String imgSrc;
  private Integer releaseYear;
  private String etc;
  private LocalDate uploadDate;
  private String address;
  private String region;
  private String content;
  private Long modelId;
  private String buyStatus;
  private String status;
  private Integer usePeriod;
  private Integer orderSeq;
  private Long accountId;
  private Boolean isDeleted;

  public static ProductDto toDto(Product product) {
    return ProductDto.builder()
        .id(product.getId())
        .sourceType(product.getSourceType() != null ? product.getSourceType().name() : null)
        .pid(product.getPid())
        .title(product.getTitle())
        .price(product.getPrice())
        .link(product.getLink())
        .imgSrc(product.getImgSrc())
        .releaseYear(product.getReleaseYear())
        .etc(product.getEtc())
        .uploadDate(product.getUploadDate())
        .address(product.getAddress())
        .region(product.getRegion())
        .content(product.getContent())
        .modelId(product.getModel() != null ? product.getModel().getId() : null)
        .buyStatus(product.getBuyStatus())
        .status(product.getStatus())
        .usePeriod(product.getUsePeriod())
        .orderSeq(product.getOrderSeq())
        .accountId(product.getAccount() != null ? product.getAccount().getId() : null)
        .isDeleted(product.getIsDeleted() == 'Y') // char → boolean 처리 예시
        .build();
  }
}

