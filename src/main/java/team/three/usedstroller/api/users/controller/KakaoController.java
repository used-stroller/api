package team.three.usedstroller.api.users.controller;

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
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.LoginWrapperDto;
import team.three.usedstroller.api.users.dto.ResponseLoginDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.dto.res.MyPageDto;
import team.three.usedstroller.api.users.service.AccountService;

@RestController
@RequiredArgsConstructor
public class KakaoController {

  private final AccountService accountService;

  @PostMapping("/api/auth/kakao")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ResponseDto<ResponseLoginDto>> kakaoLogin(@RequestBody LoginWrapperDto loginResult, HttpServletResponse response) {
    return ResponseDto.toResponseEntity(accountService.loginOrSignUp(loginResult,response));
  }

}
