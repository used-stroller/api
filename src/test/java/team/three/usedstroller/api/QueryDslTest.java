package team.three.usedstroller.api;

import static team.three.usedstroller.api.domain.QProduct.product;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import team.three.usedstroller.api.config.QueryDslConfig;
import team.three.usedstroller.api.domain.Product;
import team.three.usedstroller.api.domain.QProduct;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.ProductRes;
import team.three.usedstroller.api.repository.ProductRepositoryImpl;

@DataJpaTest
@Import(QueryDslConfig.class)
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = Replace.NONE) //실제 DB연결 해주는 설정, default가 내장형 DB
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

  @Test
  void cotainTest(){
    FilterReq filter = new FilterReq("",null,null,null,"", "", "전국",null,null,null);
    Pageable pageable = PageRequest.of(1,1);
    Page<ProductRes> products = productRepository.getProducts(filter, pageable);
  }

  @Test
  public void subQuery(){
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
  public void concat(){
    query.select(product.address.concat("_").concat(product.title))
        .from(product)
        .fetch();
  }



}
