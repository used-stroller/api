package team.three.usedstroller.api.product.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.ProductRes;

public interface CustomProductRepository {

  Page<ProductRes> getProducts(FilterReq filter, Pageable pageable);
  List<ProductRes> getProductsOnly(FilterReq filter);
  Page<ProductRes> getRecommendProductList(FilterReq filter, Pageable pageable);
}
