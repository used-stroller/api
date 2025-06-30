package team.three.usedstroller.api.rental.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import team.three.usedstroller.api.common.dto.ResponseDto;
import team.three.usedstroller.api.product.dto.res.ProductDto;
import team.three.usedstroller.api.rental.dto.RentalDto;
import team.three.usedstroller.api.rental.service.RentalService;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.LoginWrapperDto;
import team.three.usedstroller.api.users.dto.ResponseLoginDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.service.AccountService;
import team.three.usedstroller.api.utils.ResponseListDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rental")
public class RentalController {

  private final RentalService rentalService;

  @GetMapping("/list")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ResponseListDto<RentalDto>> getRentalList(
      @PageableDefault(sort="id", direction= Sort.Direction.DESC) Pageable pageable
  ) {
        return ResponseListDto.toResponseEntity(
                rentalService.getRentalList(pageable)
        );
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<ResponseDto<RentalDto>> getRentalDetails(
      @PathVariable("id") Long id)  {
    return ResponseDto.toResponseEntity(rentalService.getRentalDetails(id));
  }
}
