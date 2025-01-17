package team.three.usedstroller.api.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.product.domain.FavoriteEntity;
import team.three.usedstroller.api.users.domain.Account;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
}
