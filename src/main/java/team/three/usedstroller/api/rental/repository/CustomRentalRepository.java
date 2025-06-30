package team.three.usedstroller.api.rental.repository;

import java.util.List;

import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.rental.dto.RentalDto;

public interface CustomRentalRepository {

  List<RentalDto> getRentalList();
}
