package team.three.usedstroller.api.users.repository;

import static team.three.usedstroller.api.product.domain.QProduct.*;
import static team.three.usedstroller.api.users.domain.QAccount.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.QProduct;
import team.three.usedstroller.api.users.dto.res.MyPageDto;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements CustomAccountRepository{

	private final JPAQueryFactory query;
	private final QProduct product = QProduct.product;
	private final QFavoriteEntity favorite = QFavorite.favorite;


	@Override
	public List<Product> getSellingList() {

		return List.of();
	}

	@Override
	public List<Product> getFavorites(Long accountId) {
		JPAQuery<Product> jpaQuery = query
			.selectFrom(favorite)
			.leftJoin(product).on(favorite.productId.eq(product.id))
			.where(favorite.accountId.eq(accountId)
			);
		return jpaQuery.fetch();
	}
}
