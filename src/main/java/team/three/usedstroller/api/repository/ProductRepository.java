package team.three.usedstroller.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {
}
