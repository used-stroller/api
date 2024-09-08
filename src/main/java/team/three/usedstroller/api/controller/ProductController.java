package team.three.usedstroller.api.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.PageRequest;
import team.three.usedstroller.api.dto.ProductRes;
import team.three.usedstroller.api.dto.RestPage;
import team.three.usedstroller.api.service.CommonService;
import team.three.usedstroller.api.service.ProductService;

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

  /**
   * 중모차 추천상품 리스트
   */
  @GetMapping("/list/recommend")
  public void getRecommendProductList(FilterReq filterReq, PageRequest pageable) {
    productService.getRecommendProductList(filterReq, pageable.of());
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

//  @GetMapping("/city-name")
//  @ResponseBody
//  public GeoCodingAPi getCityName(String lat, String lon) {
//    ResponseEntity<GeoCodingAPi> cityNameByLotAndLon = reverseGeocodingApi.getCityNameByLotAndLon(lat, lon);
//  }

}
