package team.three.usedstroller.api.rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.three.usedstroller.api.rental.entity.RentalContractEntity;
import team.three.usedstroller.api.rental.entity.RentalEntity;

public interface RentalContractRepository extends JpaRepository<RentalContractEntity, Long>, CustomRentalRepository {
}
