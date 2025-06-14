package team.three.usedstroller.api.users.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import team.three.usedstroller.api.common.dto.ResponseDto;
import team.three.usedstroller.api.product.dto.res.ProductDetailDto;
import team.three.usedstroller.api.product.dto.res.ProductDto;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.LoginWrapperDto;
import team.three.usedstroller.api.users.dto.ResponseLoginDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UsersController {

  private final AccountService accountService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ResultDto> signup(@RequestBody AccountDto accountDto) {
    ResultDto resultDto = accountService.createUser(accountDto);
    return ResponseEntity.ok().body(resultDto);
  }

  @GetMapping("/mypage")
  public ResponseEntity<ResponseDto<MyPageDto>> mypage() {
    return ResponseDto.toResponseEntity(accountService.getMyPage());
  }

  @PostMapping("/mypage/update")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Boolean> updateAccount(@RequestBody AccountDto accountDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = (String) authentication.getPrincipal();
    accountService.updateAccount(email, accountDto);
    return ResponseEntity.ok().body(true);
  }

  @PostMapping("/auth/kakao")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ResponseDto<ResponseLoginDto>> kakaoLogin(@RequestBody LoginWrapperDto loginResult, HttpServletResponse response) {
    return ResponseDto.toResponseEntity(accountService.loginOrSignUp(loginResult,response));
  }

  @GetMapping("/mypage/favorites")
  public ResponseEntity<ResponseDto<List<ProductDto>>> getFavorites() {
    return ResponseDto.toResponseEntity(accountService.getFavorites());
  }

  @GetMapping("/mypage/selling-list")
  public ResponseEntity<ResponseDto<List<ProductDto>>> getSellingList() {
    return ResponseDto.toResponseEntity(accountService.getSellingList());
  }

}
