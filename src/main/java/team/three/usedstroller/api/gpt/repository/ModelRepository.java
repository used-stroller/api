package team.three.usedstroller.api.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.product.domain.Model;

public interface ModelRepository extends JpaRepository<Model, Long> {
}
