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
					(newPriceBetween(req.getMinPrice(), req.getMaxPrice())
						.or(usedPriceBetween(req.getMinPrice(), req.getMaxPrice()))),
					model.twin.eq(req.getTwin()),
					model.strollerType.eq(req.getType().toString())
				);
		return jpaQuery.fetch();
	}

	private BooleanExpression newPriceBetween(Long minPrice,Long maxPrice) {
		if(minPrice != null && maxPrice != null) {
			return model.newPrice.between(minPrice, maxPrice);
		}
		if(minPrice != null) {
			return model.newPrice.goe(minPrice);
		}
		if(maxPrice != null) {
			return model.newPrice.loe(maxPrice);
		}
		return null;
	}

	private BooleanExpression usedPriceBetween(Long minPrice,Long maxPrice) {
		if(minPrice != null && maxPrice != null) {
			return model.usedPrice.between(minPrice, maxPrice);
		}
		if(minPrice != null) {
			return model.usedPrice.goe(minPrice);
		}
		if(maxPrice != null) {
			return model.usedPrice.loe(maxPrice);
		}
		return null;
	}

}
