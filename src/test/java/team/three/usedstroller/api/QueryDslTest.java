package team.three.usedstroller.api;

import static team.three.usedstroller.api.product.domain.QModel.model;
import static team.three.usedstroller.api.product.domain.QProduct.product;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import team.three.usedstroller.api.common.config.QueryDslConfig;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.QProduct;
import team.three.usedstroller.api.product.repository.ProductRepositoryImpl;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = Replace.NONE) //실제 DB연결 해주는 설정, default가 내장형 DB
class QueryDslTest {

  @Autowired
  JPAQueryFactory query;
  @Autowired
  ProductRepositoryImpl productRepository;

  @Test
  void findById() {
    Product productOne = query
        .selectFrom(product)
        .where(product.id.eq(2L))
        .fetchOne();
    System.out.println("productOne = " + productOne);
  }

  @Test
  void like_test() {
    query.select(product)
        .from(product)
        .where(product.title.contains("테스트"))
//        .where(product.title.containsIgnoreCase("테스트"))
//        .where(product.title.like("테스트"))
//        .where(product.title.likeIgnoreCase("테스트"))
//        .where(product.title.notLike("테스트"))
        .fetch();
  }

//  @Test
//  void cotainTest(){
//    FilterReq filter = new FilterReq("",null,null,null,"", "", "전국",null,null,null);
//    Pageable pageable = PageRequest.of(1,1);
//    Page<ProductRes> products = productRepository.getProducts(filter, pageable);
//  }

  @Test
  void subQuery(){
    QProduct productSub = new QProduct("productSub");
    query.selectFrom(product)
        .where(product.id.eq(
            JPAExpressions
                .select(productSub.price)
                .from(productSub)
                .where(productSub.price.gt(100000))
        ));
  }
  @Test
  void concat(){
    query.select(product.address.concat("_").concat(product.title))
        .from(product)
        .fetch();
  }

  @Test
  void recommendPrice(){
    List<Product> result = query.selectFrom(product)
        .leftJoin(product.model, model)
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

        )
        .fetch();
    for (Product fetch1 : result) {
      System.out.println("fetch1 = " + fetch1);
    }
  }



}
