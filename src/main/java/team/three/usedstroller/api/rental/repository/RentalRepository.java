package team.three.usedstroller.api.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.three.usedstroller.api.rental.entity.RentalEntity;
import team.three.usedstroller.api.users.repository.CustomAccountRepository;

public interface RentalRepository extends JpaRepository<RentalEntity, Long>, CustomAccountRepository {
}
