package team.three.usedstroller.api.product.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import team.three.usedstroller.api.common.utils.ImageUploader;
import team.three.usedstroller.api.common.utils.SecurityUtil;
import team.three.usedstroller.api.enums.RegisterType;
import team.three.usedstroller.api.error.ApiErrorCode;
import team.three.usedstroller.api.error.ApiException;
import team.three.usedstroller.api.product.domain.FavoriteEntity;
import team.three.usedstroller.api.product.domain.ProductImageEntity;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.domain.ProductOption;
import team.three.usedstroller.api.product.domain.SourceType;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.ImageDto;
import team.three.usedstroller.api.product.dto.ProductRes;
import team.three.usedstroller.api.product.dto.RestPage;
import team.three.usedstroller.api.product.dto.req.ChangeStatusReq;
import team.three.usedstroller.api.product.dto.req.ProductUploadReq;
import team.three.usedstroller.api.product.dto.res.ProductDetailDto;
import team.three.usedstroller.api.product.repository.ProductImageRepository;
import team.three.usedstroller.api.product.repository.ProductOptionRepository;
import team.three.usedstroller.api.product.repository.ProductRepository;
import team.three.usedstroller.api.users.entity.Account;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.dto.res.SellerDto;
import team.three.usedstroller.api.users.repository.AccountRepository;
import team.three.usedstroller.api.users.repository.FavoriteRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final ImageUploader imageUploader;
  private final ProductImageRepository productImageRepository;
  private final ProductOptionRepository productOptionRepository;
  private final FavoriteRepository favoriteRepository;
  private final AccountRepository accountRepository;

  @Value("${file.upload-dir}")
  private String basicPath;

  public RestPage<ProductRes> getProducts(FilterReq filter, Pageable pageable) {
    return new RestPage<>(productRepository.getProducts(filter, pageable));
  }

  public RestPage<ProductRes> getRecommendProductList(FilterReq filterReq, Pageable pageable) {
    return new RestPage<>(productRepository.getRecommendProductList(filterReq, pageable));
  }

  public ProductDetailDto getProductDetail(Long id) {
    boolean favorite = false;
    Optional<Product> product = productRepository.findById(id);
    List<Long> options = getOptions(id);
    List<ImageDto> images = getImages(id);
    // 로그인 한 유저
    boolean login = SecurityContextHolder.getContext().getAuthentication() == null ? false : true;
    if (login) {
     favorite = favoriteRepository.findByProductIdAndAccountId(product.get().getId(),SecurityUtil.getAccountId()).isPresent();
    }


    Long sellerId = product.get().getAccount().getId();
    Optional<Account> seller = accountRepository.findById(sellerId);
    MyPageDto myPageDto = new MyPageDto();
    // 접속자 정보
    if(login) {
      Optional<Account> account = accountRepository.findById(SecurityUtil.getAccountId());
      myPageDto.setAccountId(account.get().getId());
      myPageDto.setName(account.get().getName());
    }
    return ProductDetailDto.builder()
        .id(product.get().getId())
        .createdAt(product.get().getCreatedAt())
        .updatedAt(product.get().getUpdatedAt())
        .region(product.get().getRegion())
        .options(options)
        .price(product.get().getPrice())
        .title(product.get().getTitle())
        .buyStatus(product.get().getBuyStatus())
        .imageList(images)
        .usePeriod(product.get().getUsePeriod())
        .content(product.get().getContent())
        .seller(SellerDto.builder()
            .accountId(seller.get().getId())
            .image(seller.get().getImage())
            .name(seller.get().getName())
            .build())
        .myPageDto(myPageDto)
        .favorite(favorite)
        .sellerId(product.get().getAccount().getId())
        .build();
  }

  private List<Long> getOptions(Long id) {
    List<ProductOption> optionEntities = productOptionRepository.findByProductId(id);
    List<Long> options = optionEntities.stream().map(ProductOption::getOptionId).collect(Collectors.toList());
    return options;
  }

  private List<ImageDto> getImages(Long id) {
    List<ProductImageEntity> imageListEntities = productImageRepository.findByProductIdAndIsDeleted(id,'N');
    List<ImageDto> images = imageListEntities.stream()
        .map(entity -> new ImageDto(entity.getId().toString(),entity.getSrc(),String.valueOf(entity.getOrderSeq())))
        .collect(Collectors.toList());
    return images;
  }

  @Transactional
  public Long registerProduct(ProductUploadReq req) {
    Long accountId = SecurityUtil.getAccountId();
    Optional<Account> account = accountRepository.findById(accountId);
    // 상품 저장
    Product product = Product.builder()
        .sourceType(SourceType.JUNGMOCHA)
        .uploadDate(LocalDate.now())
        .title(req.getTitle())
        .price(req.getPrice())
        .content(req.getContent())
        .buyStatus(req.getBuyStatus())
        .orderSeq(10)
        .usePeriod(req.getUsePeriod())
        .address("")
        .etc("")
        .region("")
        .isDeleted('N')
        .account(account.get())
        .status(String.valueOf(RegisterType.Ongoing))
        .build();
    productRepository.save(product);


    // image 테이블 저장
    List<MultipartFile> imageList = req.getImageList();
       String UPLOAD_DIR =basicPath +product.getId()+"/";
        int i=0;
        for (MultipartFile file : imageList) {
          ProductImageEntity imageEntity = ProductImageEntity.builder()
              .src(imageUploader.uploadFile(file,UPLOAD_DIR, product.getId()))
              .isDeleted('N')
              .orderSeq(i++)
              .product(product)
              .build();
          productImageRepository.save(imageEntity);
    }

    // product 테이블 src => 첫번째 이미지로 저장
    ProductImageEntity imageEntity = productImageRepository.findFirstByProductId(product.getId());
    product.setImgSrc(imageEntity.getSrc());
    product.setLink("/product/"+product.getId());
    productRepository.save(product);


    // option 테이블 저장
    if (req.getOptions() != null && !req.getOptions().isEmpty()) {
      for (String option : req.getOptions()) {
        productOptionRepository.save(
            ProductOption.builder()
                .product(product)
                .optionId(Long.valueOf(option))
                .build()
        );
      }
    }
    return product.getId();
  }

  private List<ImageDto> parseToImageDto (String str) {
    ObjectMapper objectMapper = new ObjectMapper();
    List<ImageDto> imagesDto = new ArrayList<>();

    try{
      imagesDto = objectMapper.readValue(str, new TypeReference<List<ImageDto>>() {});
    }
    catch (Exception e) {
      log.error("parsing 에러 : {}" , e.getMessage());
    }
    return imagesDto;
  }

  @Transactional
  public void modifyProduct(ProductUploadReq req) {
    Product product = productRepository.findById(req.getId()).orElseThrow(
        ()-> new ApiException(ApiErrorCode.PRODUCT_NOT_FOUND)
    );

    // 유저와 상품소유주 다르면 Exception
    Long ownerId = product.getAccount().getId();
    Long userId = SecurityUtil.getAccountId();
    if(ownerId != userId) {
      throw new ApiException(ApiErrorCode.NOT_PRODUCT_OWNER);
    }

        product.setTitle(req.getTitle());
        product.setPrice(req.getPrice());
        product.setContent(req.getContent());
        product.setBuyStatus(req.getBuyStatus());
        product.setUsePeriod(req.getUsePeriod());

    // image 테이블 저장
    List<MultipartFile> imageList = req.getImageList();
    String UPLOAD_DIR = basicPath+product.getId()+"/";
    Integer lastSeq = productImageRepository.findMaxOrderSeqByProductId(req.getId());
    if(imageList !=null) {
      for (MultipartFile file : imageList) {
        if(file.isEmpty()){
          continue;
        }
        ProductImageEntity imageEntity = ProductImageEntity.builder()
            .src(imageUploader.uploadFile(file,UPLOAD_DIR,product.getId()))
            .isDeleted('N')
            .orderSeq(++lastSeq)
            .product(product)
            .build();
        productImageRepository.save(imageEntity);
      }
    }

    // product 테이블 src => 첫번째 이미지로 저장
    ProductImageEntity imageEntity = productImageRepository.findFirstByProductId(product.getId());
    product.setImgSrc(imageEntity.getSrc());
    product.setLink("/product/"+product.getId());
    productRepository.save(product);

    // 이미지 삭제
    if (req.getDeleted() != null && !req.getDeleted().isEmpty()) {
      for (String id : req.getDeleted()) {
        ProductImageEntity productImg = productImageRepository.findById(Long.valueOf(id))
            .orElseThrow(
                () -> new ApiException(ApiErrorCode.PRODUCT_NOT_FOUND)
            );
        productImg.setIsDeleted('Y');
      }
    }

    // option 테이블 저장
    productOptionRepository.deleteByProductId(req.getId());
    if (req.getOptions() != null && !req.getOptions().isEmpty()) {
      for (String option : req.getOptions()) {
        productOptionRepository.save(
            ProductOption.builder()
                .product(product)
                .optionId(Long.valueOf(option))
                .build()
        );
      }
    }
  }

  @Transactional
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id).orElseThrow(
        () -> new ApiException(ApiErrorCode.PRODUCT_NOT_FOUND)
    );
    product.setIsDeleted('Y');
  }

  @Transactional
  public void addFavoriteProduct(Long productId) {
    Long memberId = SecurityUtil.getAccountId();
    favoriteRepository.save(
        FavoriteEntity.builder()
            .productId(productId)
            .accountId(memberId)
            .build());
  }

  @Transactional
  public void deleteFavoriteProduct(Long productId) {
    Long memberId = SecurityUtil.getAccountId();
    favoriteRepository.deleteByProductIdAndAccountId(productId,memberId);
  }

  @Transactional
  public void changeStatus(ChangeStatusReq request) {
    Product productEntity = productRepository.findById(request.getId()).orElseThrow(() -> new ApiException(ApiErrorCode.PRODUCT_NOT_FOUND));
    productEntity.setStatus(request.getStatusType().toString());
  }

  //  public void modify(List<MultipartFile> newImages, String existringImages, Set deletedImages, String newImageData, Long productId) {
//
//    List<ImageDto> existingDto = parseToImageDto(existringImages);
//    List<ImageDto> newImageDataDto = parseToImageDto(newImageData);
//    existingDto = existingDto.stream().filter(image -> !deletedImages.contains(image.getId())).collect(
//        Collectors.toList());
//    Product product = productRepository.findById(productId).orElse(null);
//    String UPLOAD_DIR = basicPath+productId+"/";
//    // 각 새 이미지와 메타 데이터 매핑
//    for (int i = 0; i<newImages.size(); i++) {
//      MultipartFile file = newImages.get(i);
//      imageUploader.uploadFile(file,UPLOAD_DIR);
//      productImageRepository.save(ProductImageEntity.builder()
//          .orderSeq(Integer.parseInt(newImageDataDto.get(i).getOrderSeq()))
//          .src(newImageDataDto.get(i).getSrc())
//          .product(product)
//          .build());
//
//      // 기존 이미지 인덱스 업데이트
//
//
//      // 삭제된 이미지 처리
//
//
//      // product save()
//    }
//  }
  //  @Async("securityAsyncExecutor")  // @Async에서 지정한 Executor 사용
  //  public CompletableFuture<ProductDetailDto> getProductDetail(Long id, Authentication authentication) {
  //    log.info("getDetail함수 실행됨");
  //
  //    Optional<Product> e = productRepository.findById(id);
  //    List<Long> options = getOptions(id);
  //    List<ImageDto> images = getImages(id);
  //
  //    log.info("Before getAccountId: {}", authentication);
  //
  //    Account account = e.get().getAccount();
  //    boolean favorite = favoriteRepository.findByProductIdAndAccountId(e.get().getId(),SecurityUtil.getAccountId()).isPresent();
  //
  //    ProductDetailDto productDetailDto = ProductDetailDto.builder()
  //        .createdAt(e.get().getCreatedAt())
  //        .updatedAt(e.get().getUpdatedAt())
  //        .region(e.get().getRegion())
  //        .options(options)
  //        .price(e.get().getPrice())
  //        .title(e.get().getTitle())
  //        .buyStatus(e.get().getBuyStatus())
  //        .imageList(images)
  //        .usePeriod(e.get().getUsePeriod())
  //        .content(e.get().getContent())
  //        .myPageDto(MyPageDto.builder()
  //            .accountId(account.getId())
  //            .image(account.getImage())
  //            .name(account.getName())
  //            .build())
  //        .build();
  //
  //    return CompletableFuture.completedFuture(productDetailDto);
  //  }
}
