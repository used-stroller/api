package team.three.usedstroller.api.security.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import team.three.usedstroller.api.security.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Boolean existsByToken(String token);

  @Transactional
  void deleteByToken(String token);

  @Modifying
  @Transactional
  @Query("delete from RefreshToken r where r.expiration <= :now")
  void deleteAllExpiredToken(@Param("now") Date now);

  @Modifying
  @Transactional
  void deleteAllByEmail(String email);
}
