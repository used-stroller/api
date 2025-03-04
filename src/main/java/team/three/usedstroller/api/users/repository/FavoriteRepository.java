package team.three.usedstroller.api.users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.product.domain.FavoriteEntity;
import team.three.usedstroller.api.users.domain.Account;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
	void deleteByProductIdAndAccountId(long productId, long accountId);

	Optional<FavoriteEntity> findByProductIdAndAccountId(long productId, long accountId);
	List<FavoriteEntity> findByAccountId(long accountId);
}
