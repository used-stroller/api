package team.three.usedstroller.api.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import team.three.usedstroller.api.product.domain.ProductImageEntity;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
	ProductImageEntity findFirstByProductId(Long productId);

	List<ProductImageEntity> findByProductId(Long id);
}
