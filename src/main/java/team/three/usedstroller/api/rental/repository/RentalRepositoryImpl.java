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
import team.three.usedstroller.api.product.domain.QModel;
import team.three.usedstroller.api.rental.dto.RentalDto;
import team.three.usedstroller.api.rental.entity.QRentalContractInfoEntity;
import team.three.usedstroller.api.rental.entity.QRentalEntity;
import team.three.usedstroller.api.rental.entity.QRentalImageEntity;
import team.three.usedstroller.api.rental.entity.RentalEntity;

@Repository
@RequiredArgsConstructor
public class RentalRepositoryImpl implements CustomRentalRepository {

	private final JPAQueryFactory query;
	private final QRentalEntity rentalEntity = QRentalEntity.rentalEntity;
	private final QModel model = QModel.model;
	private final QRentalImageEntity rentalImageEntity = QRentalImageEntity.rentalImageEntity;
	private final QRentalContractInfoEntity contractInfoEntity = QRentalContractInfoEntity.rentalContractInfoEntity;

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
			rentalEntity.productionDate.as("productionDate"),
			rentalEntity.model.strollerType.as("strollerType"),
			rentalEntity.grade.as("grade"),
			rentalEntity.model.weight.as("weight"),
			rentalEntity.model.size.as("size"),
			contractInfoEntity.rentalStart.as("rentalStart"),
			contractInfoEntity.rentalEnd.as("rentalEnd")
		))
		.from(rentalEntity)
			.leftJoin(rentalEntity.model, model)
			.leftJoin(contractInfoEntity).on(contractInfoEntity.rental.id.eq(rentalEntity.id))
			.where(rentalEntity.deleted.eq(false))
			.orderBy(rentalEntity.id.desc());

	     Long totalCount = query
		.select(rentalEntity.count())
		.from(rentalEntity)
		.fetchOne();

		jpaQuery.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<RentalDto> rentalList = jpaQuery.fetch();
		return new PageImpl<>(rentalList, pageable, totalCount);
	}

	@Override
	public RentalDto getRentalDetails(Long id) {
		RentalEntity entity = query
			.selectFrom(rentalEntity)
			.leftJoin(rentalEntity.images, rentalImageEntity).fetchJoin()
			.where(rentalEntity.id.eq(id)).orderBy(rentalImageEntity.orderSeq.asc()) // ✅ 정렬 추가
			.fetchOne();

		return RentalDto.from(entity);
	}
}
