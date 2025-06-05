package team.three.usedstroller.api.product.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.ProductOption;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
  List<ProductOption> findByProductId(Long productId);

  @Modifying
  @Transactional
  @Query("DELETE FROM ProductOption p WHERE p.product.id = :productId")
  void deleteByProductId(@Param("productId") Long productId);
}
