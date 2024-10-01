package team.three.usedstroller.api.product.repository;

import static team.three.usedstroller.api.product.domain.QModel.model;
import static team.three.usedstroller.api.product.domain.QProduct.product;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.SourceType;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.ProductRes;


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
            applyDefaultRegion(filter.getRegion(), filter.getFixedAddress(), filter.getDetailAddress()),
            applyRegion(filter.getRegion()),
            applyBrand(filter.getBrand()),
            applyModel(filter.getModel()),
            applyPeriod(filter.getPeriod()),
            applyNotNullUploadDate(pageable.getSort()));

    int totalCount = jpaQuery.fetch().size();

    List<ProductRes> products = jpaQuery
        .orderBy(getOrderBy(pageable.getSort()))
        .orderBy(product.uploadDate.desc().nullsLast())
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

    return jpaQuery
        .fetch()
        .stream()
        .map(ProductRes::of)
        .toList();
  }

  @Override
  public Page<ProductRes> getRecommendProductList(FilterReq filter,Pageable pageable) {
    JPAQuery<Product> jpaQuery = query.select(product)
        .from(product)
        .leftJoin(product.model,model)
        .where(
            product.model.isNotNull(),
            model.recommendPrice.isNotNull(),
            // recommendPrice * 0.9를 BigDecimal로 처리한 후 Long으로 변환
            product.price.gt(
                Expressions.numberTemplate(BigDecimal.class, "({0} * 0.7)", model.recommendPrice).longValue()
            ),
            // recommendPrice * 1.1을 BigDecimal로 처리한 후 Long으로 변환
            product.price.lt(
                Expressions.numberTemplate(BigDecimal.class, "({0} * 1.2)", model.recommendPrice).longValue()
            ),
            product.title.notLike("%배시넷").and(product.title.notLike("%베시넷%"))
        );
      int totalCount = jpaQuery.fetch().size();

      List<ProductRes> products = jpaQuery
          .orderBy(getOrderBy(pageable.getSort()))
          .orderBy(product.uploadDate.desc().nullsLast())
          .offset(pageable.getOffset())
          .limit(ObjectUtils.isEmpty(pageable.getPageSize()) ? 10: pageable.getPageSize())
          .fetch()
          .stream()
          .map(ProductRes::of)
          .toList();
    return new PageImpl<>(products, pageable, totalCount);
  }

  /**
   * 1. 사용자가 직접 동네를 검색하면(region) 자동으로 받아온 당근 지역 필터링을 제외하고 모든 데이터를 기준으로 위치 검색한다.
   * 2. 직접 검색이 없다면, 위경도 값을 기준으로 아래 당근 위치 필터링을 적용한다.(당근 제품만 위치 적용됨)
    */
  private Predicate applyDefaultRegion(String region, String fixedAddress, String detailAddress) {
    if (StringUtils.hasText(region) || isEmpty(fixedAddress, detailAddress)) {
      return null;
    }
    BooleanExpression fixedListExpression = Arrays.stream(fixedAddress.split(","))
        .map(product.region::contains)
        .reduce(BooleanExpression::or)
        .orElse(null);
    BooleanExpression detailListExpression = Arrays.stream(detailAddress.split(","))
        .map(product.region::contains)
        .reduce(BooleanExpression::or)
        .orElse(null);
    return product.sourceType.eq(SourceType.CARROT)
        .and(fixedListExpression)
        .and(detailListExpression)
        .or(product.sourceType.ne(SourceType.CARROT));
  }

  private static boolean isEmpty(String fixedAddress, String detailAddress) {
    return !StringUtils.hasText(fixedAddress) && !StringUtils.hasText(detailAddress);
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

  /**
   * uploadDate 최신순 정렬 조건이 있으면, null 값을 제외하고 정렬한다.
   */
  private Predicate applyNotNullUploadDate(Sort sort) {
    return sort.stream()
        .anyMatch(order -> order.getProperty().equalsIgnoreCase("uploadDate"))
        ? product.uploadDate.isNotNull()
        : null;
  }

  private OrderSpecifier<?>[] getOrderBy(Sort sort) {
    return Stream.concat(
        sort.stream()
            .map(order -> new OrderSpecifier<>(
                order.isAscending() ? Order.ASC : Order.DESC,
                Expressions.stringPath(order.getProperty())
            )),
        Stream.of(new OrderSpecifier<>(
            Order.DESC,
            product.sourceType.when(SourceType.NAVER).then(0).otherwise(1)
        ))
    ).toArray(OrderSpecifier[]::new);
  }
}
