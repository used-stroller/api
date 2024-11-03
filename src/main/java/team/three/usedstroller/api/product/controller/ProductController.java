package team.three.usedstroller.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.PageRequest;
import team.three.usedstroller.api.product.dto.ProductRes;
import team.three.usedstroller.api.product.dto.RestPage;
import team.three.usedstroller.api.product.dto.req.ProductUploadReq;
import team.three.usedstroller.api.product.dto.res.ProductDetailDto;
import team.three.usedstroller.api.product.service.CommonService;
import team.three.usedstroller.api.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;
  private final CommonService commonService;

  /**
   * 상품리스트 가져오기 Top10 브랜드, 모델리스트, 토탈카운트 포함 동네,가격,모델명,기간,브랜드,사이트별 파라미터 다 들어가는 한방 쿼리
   *
   * @param filter 제목, 수집처, 가격(min, max), 브랜드, 모델, 지역, 등록일자
   * @param pageable 페이지, 사이즈, 정렬(예시: "price,desc,title,asc")
   */
  @GetMapping("/list")
  public RestPage<ProductRes> getProducts(FilterReq filter, PageRequest pageable) {
    return productService.getProducts(filter, pageable.of());
  }

  @Operation(summary = "상품 상세 데이터")
  @GetMapping("/get")
  public ProductDetailDto getProductDetail(Long id) {
    return productService.getProductDetail(id);
  }

  /**
   * 중모차 추천상품 리스트
   */
  @GetMapping("/list/recommend")
  public RestPage<ProductRes> getRecommendProductList(FilterReq filterReq, PageRequest pageable) {
    return productService.getRecommendProductList(filterReq, pageable.of());
  }

  /**
   * 브랜드별 모델리스트 파라미터 브랜드 id
   */
  @GetMapping("/model-list")
  public void getModelList() {
  }

  @PostMapping("/image-download")
  public void downloadImage(FilterReq filter) throws IOException {
    commonService.downloadImage(filter);
  }

  @PostMapping(value="/register", consumes = {"multipart/form-data"})
  public void registerProduct(
       @RequestPart(value = "imageList",required = false) List<MultipartFile> imageList
      ,@RequestParam("title") String title
      ,@RequestParam("price") String price
      ,@RequestParam("content") String content
      ,@RequestParam("buyStatus") String buyStatus
      ,@RequestParam(value = "options",required = false) List<String> options
      ,@RequestParam("usePeriod") String usePeriod
      //,@RequestParam("address") String address
      //,@RequestParam("region") String region  => 주소 api 적용 후 나중에 추가
  ){
    ProductUploadReq req = ProductUploadReq.builder()
        .title(title)
        .price(Long.valueOf(price))
        .content(content)
        .buyStatus(buyStatus)
        .options(options)
        .usePeriod(Integer.parseInt(usePeriod))
        .imageList(imageList)
        .build();

    productService.registerProduct(req);
  }


  @Operation(summary = "상품 수정")
  @PostMapping(value = "/file/multipartFile/modify",consumes = {"multipart/form-data"})
  public void multipartFileUpload(
      @RequestPart(value = "newImages",required = false) List<MultipartFile> newImages, // 새로 input 된 파일
      @RequestParam(value = "newImageData",required = false) String newImageData, // input된 이미지의 index 정보
      @RequestParam("existingImages") String existingImages, // DB에 있었던 이미지
      @RequestParam(value = "deletedImages",required = false) Set deletedImages, // 삭제된 image id 값
      @RequestParam(value = "productId") Long productId
  ) {
    productService.modify(newImages,existingImages,deletedImages,newImageData,productId);
  }

}
