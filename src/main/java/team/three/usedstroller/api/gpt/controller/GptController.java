package team.three.usedstroller.api.gpt.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.three.usedstroller.api.common.dto.ResponseDto;
import team.three.usedstroller.api.gpt.dto.UserInputReqDto;
import team.three.usedstroller.api.gpt.service.GptService;
import team.three.usedstroller.api.product.dto.FilterReq;
import team.three.usedstroller.api.product.dto.PageRequest;
import team.three.usedstroller.api.product.dto.ProductRes;
import team.three.usedstroller.api.product.dto.RestPage;
import team.three.usedstroller.api.product.dto.req.ChangeStatusReq;
import team.three.usedstroller.api.product.dto.req.ProductUploadReq;
import team.three.usedstroller.api.product.dto.res.ProductDetailDto;
import team.three.usedstroller.api.product.service.CommonService;
import team.three.usedstroller.api.product.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GptController {

  private final GptService gptService;

  @GetMapping("/recommend")
  public ResponseEntity<ResponseDto<?>> recommendStroller(UserInputReqDto req) {
    return gptService.recommend(req);
  }

}
