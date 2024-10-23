package team.three.usedstroller.api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.three.usedstroller.api.product.domain.ProductImageEntity;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
	ProductImageEntity findFirstByProductId(Long productId);
}
