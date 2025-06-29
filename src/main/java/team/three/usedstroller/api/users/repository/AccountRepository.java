package team.three.usedstroller.api.users.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.users.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>, CustomAccountRepository {
  boolean existsAccountByEmail(String email);
  boolean existsAccountByKakaoId(String kakaoId);
  Optional<Account> findByEmail(String email);
  Optional<Account> findByKakaoId(String kakaoId);
}
