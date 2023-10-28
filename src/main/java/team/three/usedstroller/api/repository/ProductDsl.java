package team.three.usedstroller.api.repository;

import static team.three.usedstroller.api.domain.QProduct.product;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.util.ObjectUtils;
import team.three.usedstroller.api.domain.SourceType;
import team.three.usedstroller.api.dto.FilterReq;

public class ProductDsl<T> {

  private final JPAQuery<T> jpaQuery;

  public ProductDsl(JPAQuery<T> jpaQuery, FilterReq filter) {
    this.jpaQuery = jpaQuery
        .from(product);

    applyKeyword(filter.getKeyword());
    applySourceType(filter.getSourceType());
    applyPriceRange(filter.getMinPrice(), filter.getMaxPrice());
    applyTown(filter.getTown());
  }

  private void applyPriceRange(Long minPrice, Long maxPrice) {
    if (minPrice != null && maxPrice != null) {
      jpaQuery.where(product.price.between(minPrice, maxPrice));
    } else if (minPrice != null) {
      jpaQuery.where(product.price.goe(minPrice));
    } else if (maxPrice != null) {
      jpaQuery.where(product.price.loe(maxPrice));
    }
  }

  private void applySourceType(SourceType sourceType) {
    if (!ObjectUtils.isEmpty(sourceType)) {
      jpaQuery.where(product.sourceType.eq(sourceType));
    }
  }

  private void applyKeyword(String keyword) {
    if (keyword != null) {
      jpaQuery.where(product.title.containsIgnoreCase(keyword)
          .or(product.content.containsIgnoreCase(keyword))
          .or(product.etc.containsIgnoreCase(keyword)));
    }
  }

  private void applyTown(String town) {
    if (town != null) {
      jpaQuery.where(product.address.containsIgnoreCase(town));
    }
  }

  public JPAQuery<T> getDsl() {
    return jpaQuery;
  }
}
