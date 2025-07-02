package team.three.usedstroller.api.rental.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.three.usedstroller.api.rental.dto.RentalDto;
import team.three.usedstroller.api.rental.entity.QRentalEntity;

@Repository
@RequiredArgsConstructor
public class RentalRepositoryImpl implements CustomRentalRepository {

	private final JPAQueryFactory query;
	private final QRentalEntity rentalEntity = QRentalEntity.rentalEntity;

	@Override
	public Page<RentalDto> getRentalList(Pageable pageable) {
	JPAQuery<RentalDto> jpaQuery = query.select(Projections.fields(RentalDto.class,
			rentalEntity.id.as("id"),
			rentalEntity.code.as("code"),
			rentalEntity.productName.as("productName"),
			rentalEntity.src.as("src"),
			rentalEntity.color.as("color"),
			rentalEntity.rentalPrice.as("rentalPrice"),
			rentalEntity.isRentable.as("isRentable"),
			rentalEntity.productionDate.as("productionDate")
		))
		.from(rentalEntity);

	     Long totalCount = query
		.select(rentalEntity.count())
		.from(rentalEntity)
		.fetchOne();

		jpaQuery.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<RentalDto> rentalList = jpaQuery.fetch();
		return new PageImpl<>(rentalList, pageable, totalCount);
	}
}
