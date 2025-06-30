package team.three.usedstroller.api.rental.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.QFavoriteEntity;
import team.three.usedstroller.api.product.domain.QProduct;
import team.three.usedstroller.api.rental.dto.RentalDto;
import team.three.usedstroller.api.rental.entity.QRentalEntity;

@Repository
@RequiredArgsConstructor
public class RentalRepositoryImpl implements CustomRentalRepository {

	private final JPAQueryFactory query;
	private final QRentalEntity rental = QRentalEntity.rentalEntity;
	private final QProduct product = QProduct.product;

	@Override
	public List<RentalDto> getRentalList() {
		return List.of();
	}
}
