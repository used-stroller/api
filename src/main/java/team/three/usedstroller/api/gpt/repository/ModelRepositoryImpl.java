package team.three.usedstroller.api.gpt.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.three.usedstroller.api.gpt.dto.UserInputReqDto;
import team.three.usedstroller.api.product.domain.Model;
import team.three.usedstroller.api.product.domain.QModel;

@Repository
@RequiredArgsConstructor
public class ModelRepositoryImpl {

	private final JPAQueryFactory query;
	private final QModel model = QModel.model;

	public List<Model> filterByHardCondition(UserInputReqDto req) {
		JPAQuery<Model> jpaQuery = query
				.selectFrom(model)
				.where(
					model.minAge.loe(req.getAge()),
					model.maxAge.goe(req.getAge()),
					(newPriceBelow(req.getMaxPriceNew())
						.or(usedPriceBelow(req.getMaxPriceUsed()))),
					model.twin.eq(req.getTwin()),
					model.strollerType.eq(req.getType().toString())
				)
				.limit(10)
			;

		return jpaQuery.fetch();
	}

	private BooleanExpression newPriceBelow(Long maxPrice) {
		return (maxPrice != null) ? model.newPrice.loe(maxPrice) : null;
	}

	private BooleanExpression usedPriceBelow(Long maxPrice) {
		return (maxPrice != null) ? model.usedPrice.loe(maxPrice) : null;
	}

}
