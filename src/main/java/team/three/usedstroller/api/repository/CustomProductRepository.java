package team.three.usedstroller.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.ProductRes;

public interface CustomProductRepository {

  Page<ProductRes> getProducts(FilterReq filter, Pageable pageable);
}