// package team.three.usedstroller.api.rental.repository;
//
// import java.util.List;
//
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Repository;
//
// import com.querydsl.core.types.Projections;
// import com.querydsl.jpa.impl.JPAQuery;
// import com.querydsl.jpa.impl.JPAQueryFactory;
//
// import lombok.RequiredArgsConstructor;
// import team.three.usedstroller.api.product.domain.QModel;
// import team.three.usedstroller.api.rental.dto.RentalDto;
// import team.three.usedstroller.api.rental.entity.QRentalContractInfoEntity;
// import team.three.usedstroller.api.rental.entity.QRentalEntity;
// import team.three.usedstroller.api.rental.entity.QRentalImageEntity;
// import team.three.usedstroller.api.rental.entity.RentalContractInfoEntity;
// import team.three.usedstroller.api.rental.entity.RentalEntity;
//
// @Repository
// @RequiredArgsConstructor
// public class RentalContractRepositoryImpl implements CustomRentalRepository {
//
// 	private final JPAQueryFactory query;
// 	private final QRentalEntity rentalEntity = QRentalEntity.rentalEntity;
// 	private final QModel model = QModel.model;
// 	private final QRentalImageEntity rentalImageEntity = QRentalImageEntity.rentalImageEntity;
// 	private final QRentalContractInfoEntity contractEntity = QRentalContractInfoEntity.rentalContractInfoEntity;
//
// 	@Override
// 	public RentalContractInfoEntity getContract(Long rentalId) {
// 		RentalContractInfoEntity entity = query
// 			.selectFrom(contractEntity)
// 			.leftJoin(contractEntity.rental, rentalEntity).fetchJoin()
// 			.where(rentalEntity.id.eq(rentalId)).orderBy(contractEntity.id.desc()
// 				.and(contractEntity.delete.eq(false))
// 			)
// 			.fetchFirst();
//
// 		return RentalDto.from(entity);
// 	}
// }
