package team.three.usedstroller.api;

import static team.three.usedstroller.api.domain.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import team.three.usedstroller.api.config.QueryDslConfig;
import team.three.usedstroller.api.domain.Product;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = Replace.NONE) //실제 DB연결 해주는 설정, default가 내장형 DB
class QueryDslTest {

  @Autowired
  JPAQueryFactory query;

  @Test
  void findById() {
    Product productOne = query
        .selectFrom(product)
        .where(product.id.eq(1L))
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

}
