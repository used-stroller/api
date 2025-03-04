package team.three.usedstroller.api.product.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
}
