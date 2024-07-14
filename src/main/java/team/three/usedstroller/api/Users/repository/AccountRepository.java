package team.three.usedstroller.api.Users.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team.three.usedstroller.api.Users.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
  boolean existsAccountByEmail(String email);
  Optional<Account> findByEmail(String email);
}
