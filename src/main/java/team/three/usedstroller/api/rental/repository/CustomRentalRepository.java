package team.three.usedstroller.api.rental.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.rental.dto.RentalDto;

public interface CustomRentalRepository {
  Page<RentalDto> getRentalList(Pageable pageable);

  RentalDto getRentalDetails(Long id);
}
