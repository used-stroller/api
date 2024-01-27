package team.three.usedstroller.api.repository;

import static team.three.usedstroller.api.domain.QProduct.product;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import team.three.usedstroller.api.domain.SourceType;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.ProductRes;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements CustomProductRepository {

  private final JPAQueryFactory query;

  @Override
  public Page<ProductRes> getProducts(FilterReq filter, Pageable pageable) {
    List<ProductRes> products = query.select(product)
        .from(product)
        .where(applyKeyword(filter.getKeyword()),
            applySourceType(filter.getSourceType()),
            applyPriceRange(filter.getMinPrice(), filter.getMaxPrice()),
            applyTown(filter.getTown()),
            applyBrand(filter.getBrand()),
            applyModel(filter.getModel()),
            applyPeriod(filter.getPeriod()))
        .orderBy(getOrderBy(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(ObjectUtils.isEmpty(pageable.getPageSize()) ? 10: pageable.getPageSize())
        .fetch()
        .stream()
        .map(ProductRes::of)
        .toList();

    return new PageImpl<>(products, pageable, products.size());
  }

  private BooleanExpression applyPeriod(Integer period) {
    if (period != null) {
      LocalDate periodDate = LocalDate.now().minusDays(period);
      return product.uploadDate.goe(periodDate);
    }
    return null;
  }

  private BooleanExpression applyBrand(List<String> brand) {
    if (!CollectionUtils.isEmpty(brand)) {
      return product.title.in(brand);
    }
    return null;
  }

  private BooleanExpression applyModel(List<String> model) {
    if (!CollectionUtils.isEmpty(model)) {
      return product.title.in(model);
    }
    return null;
  }

  private BooleanExpression applyTown(String town) {
    if (StringUtils.hasText(town)) {
      return product.address.containsIgnoreCase(town);
    }
    return null;
  }

  private BooleanExpression applyPriceRange(Long minPrice, Long maxPrice) {
    if (minPrice != null && maxPrice != null) {
      return product.price.between(minPrice, maxPrice);
    } else if (minPrice != null) {
      return product.price.goe(minPrice);
    } else if (maxPrice != null) {
      return product.price.loe(maxPrice);
    }
    return null;
  }

  private BooleanExpression applySourceType(List<SourceType> sourceType) {
    if (!CollectionUtils.isEmpty(sourceType)) {
      return product.sourceType.in(sourceType);
    }
    return null;
  }

  private BooleanExpression applyKeyword(String keyword) {
    if (StringUtils.hasText(keyword)) {
      return product.title.containsIgnoreCase(keyword)
          .or(product.content.containsIgnoreCase(keyword))
          .or(product.etc.containsIgnoreCase(keyword));
    }
    return null;
  }

  private OrderSpecifier<?>[] getOrderBy(Sort sort) {
    return sort.stream()
        .map(order -> new OrderSpecifier<>(
            order.isAscending() ? Order.ASC : Order.DESC,
            Expressions.stringPath(order.getProperty())
        )).toArray(OrderSpecifier[]::new);
  }
}
