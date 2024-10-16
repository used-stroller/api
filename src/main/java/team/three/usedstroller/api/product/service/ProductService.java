package team.three.usedstroller.api.product.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.three.usedstroller.api.common.utils.ImageUploader;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.ProductRes;
import team.three.usedstroller.api.product.dto.RestPage;
import team.three.usedstroller.api.product.repository.ProductRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final ImageUploader imageUploader;

//  @Cacheable(value = "products",
//      key = "{#filter, #pageable}",
//      unless = "#result == null")
  public RestPage<ProductRes> getProducts(FilterReq filter, Pageable pageable) {
    return new RestPage<>(productRepository.getProducts(filter, pageable));
  }

  public RestPage<ProductRes> getRecommendProductList(FilterReq filterReq, Pageable pageable) {
    return new RestPage<>(productRepository.getRecommendProductList(filterReq, pageable));
  }

  public void uplpoadImage(List<MultipartFile> files) {
    imageUploader.uploadFile(files);
  }
}
