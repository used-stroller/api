package team.three.usedstroller.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.ProductRes;
import team.three.usedstroller.api.dto.RestPage;
import team.three.usedstroller.api.repository.ProductRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;

//  @Cacheable(value = "products",
//      key = "{#filter, #pageable}",
//      unless = "#result == null")
  public RestPage<ProductRes> getProducts(FilterReq filter, Pageable pageable) {
    return new RestPage<>(productRepository.getProducts(filter, pageable));
  }

  public List<ProductRes> getRecommendProductList(FilterReq filterReq, Pageable pageable) {
    return productRepository.getRecommendProductList(filterReq,pageable);
  }
}
