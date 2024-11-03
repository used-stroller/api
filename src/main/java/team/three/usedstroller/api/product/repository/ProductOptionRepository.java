package team.three.usedstroller.api.product.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.ProductOption;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
  List<ProductOption> findByProductId(Long productId);
}
