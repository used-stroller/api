package team.three.usedstroller.api.repository;

import static team.three.usedstroller.api.domain.QProduct.product;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
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
import team.three.usedstroller.api.domain.Product;
import team.three.usedstroller.api.domain.SourceType;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.ProductRes;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements CustomProductRepository {

  private final JPAQueryFactory query;

  @Override
  public Page<ProductRes> getProducts(FilterReq filter, Pageable pageable) {
    JPAQuery<Product> jpaQuery = query.select(product)
        .from(product)
        .where(applyKeyword(filter.getKeyword()),
            applySourceType(filter.getSourceType()),
            applyPriceRange(filter.getMinPrice(), filter.getMaxPrice()),
            applyRegion(filter.getRegion()),
            applyBrand(filter.getBrand()),
            applyModel(filter.getModel()),
            applyPeriod(filter.getPeriod()));

    int totalCount = jpaQuery.fetch().size();

    List<ProductRes> products = jpaQuery
        .orderBy(getOrderBy(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(ObjectUtils.isEmpty(pageable.getPageSize()) ? 10: pageable.getPageSize())
        .fetch()
        .stream()
        .map(ProductRes::of)
        .toList();

    return new PageImpl<>(products, pageable, totalCount);
  }

  @Override
  public List<ProductRes> getProductsOnly(FilterReq filter) {
    JPAQuery<Product> jpaQuery = query.select(product)
        .from(product)
        .where(applyKeyword(filter.getKeyword()),
            applySourceType(filter.getSourceType()),
            applyPriceRange(filter.getMinPrice(), filter.getMaxPrice()),
            applyRegion(filter.getRegion()),
            applyBrand(filter.getBrand()),
            applyModel(filter.getModel()),
            applyPeriod(filter.getPeriod()));

    List<ProductRes> products = jpaQuery
        .fetch()
        .stream()
        .map(ProductRes::of)
        .toList();
    return products;
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
      return brand.stream()
          .map(product.title::containsIgnoreCase)
          .reduce(BooleanExpression::or)
          .orElse(null);
    }
    return null;
  }

  private BooleanExpression applyModel(List<String> model) {
    if (!CollectionUtils.isEmpty(model)) {
      return model.stream()
          .map(product.title::containsIgnoreCase)
          .reduce(BooleanExpression::or)
          .orElse(null);
    }
    return null;
  }

  private BooleanExpression applyRegion(String region) {
    if (StringUtils.hasText(region)) {
      return product.region.containsIgnoreCase(region);
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
