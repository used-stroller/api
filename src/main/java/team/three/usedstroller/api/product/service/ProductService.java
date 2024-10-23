package team.three.usedstroller.api.product.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import team.three.usedstroller.api.common.utils.ImageUploader;
import team.three.usedstroller.api.product.domain.ProductImageEntity;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.ProductRes;
import team.three.usedstroller.api.product.dto.RestPage;
import team.three.usedstroller.api.product.dto.req.ProductUploadReq;
import team.three.usedstroller.api.product.repository.ProductImageRepository;
import team.three.usedstroller.api.product.repository.ProductRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final ImageUploader imageUploader;
  private final ProductImageRepository productImageRepository;

//  @Cacheable(value = "products",
//      key = "{#filter, #pageable}",
//      unless = "#result == null")
  public RestPage<ProductRes> getProducts(FilterReq filter, Pageable pageable) {
    return new RestPage<>(productRepository.getProducts(filter, pageable));
  }

  public RestPage<ProductRes> getRecommendProductList(FilterReq filterReq, Pageable pageable) {
    return new RestPage<>(productRepository.getRecommendProductList(filterReq, pageable));
  }



  public ProductRes getProductDetail(Long id) {
    return null;
  }

  @Transactional
  public void registerProduct(ProductUploadReq req) {

    // 상품 저장
    Product product = Product.builder()
        .sourceType(req.getSourceType())
        .title(req.getTitle())
        .price(req.getPrice())
        .content(req.getContent())
        .buyStatus(req.getBuyStatus())
        .order(req.getOrder())
        .usePeriod(req.getUsePeriod())
        .build();
    productRepository.save(product);


    // image 테이블 저장
    List<MultipartFile> imageList = req.getImageList();
    int i=0;
    for (MultipartFile file : imageList) {
      ProductImageEntity imageEntity = ProductImageEntity.builder()
          .src(imageUploader.uploadFile(file))
          .order(i++)
          .product(product)
          .build();
      productImageRepository.save(imageEntity);
    }

    // product 테이블 src => 첫번째 이미지로 저장
    ProductImageEntity imageEntity = productImageRepository.findFirstByProductId(product.getId());
    product.setImgSrc(imageEntity.getSrc());
    productRepository.save(product);
  }
}
