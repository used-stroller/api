package team.three.usedstroller.api.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.dto.FilterReq;
import team.three.usedstroller.api.dto.ProductRes;
import team.three.usedstroller.api.repository.ProductRepository;
import team.three.usedstroller.api.utils.ImageDownloader;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

  private final ImageDownloader imageDownloader;
  private final ProductRepository productRepository;

  public void downloadImage(FilterReq filter) throws IOException {
    List<ProductRes> productList = productRepository.getProductsOnly(filter);
    int complete=0;
    String model = filter.getKeyword();
    for (int i = 0; i <productList.size(); i++) {
      String url = productList.get(i).getImgSrc();
      imageDownloader.convertToFile(url,model,i);
      complete++;
    }
    log.info("모델명: {},이미지 다운 완료: {}건",model,complete);
  }
}
