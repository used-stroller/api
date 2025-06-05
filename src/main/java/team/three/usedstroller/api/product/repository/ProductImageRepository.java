package team.three.usedstroller.api.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.three.usedstroller.api.product.domain.ProductImageEntity;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
	ProductImageEntity findFirstByProductId(Long productId);

	List<ProductImageEntity> findByProductId(Long id);

  List<ProductImageEntity> findByProductIdAndIsDeleted(Long id, char n);

	@Query("SELECT MAX(p.orderSeq) FROM ProductImageEntity p WHERE p.product.id = :productId")
	Integer findMaxOrderSeqByProductId(@Param("productId") Long productId);
}
