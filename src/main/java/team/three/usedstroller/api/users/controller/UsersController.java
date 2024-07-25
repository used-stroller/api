package team.three.usedstroller.api.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.users.dto.AccountDto;
import team.three.usedstroller.api.users.dto.ResultDto;
import team.three.usedstroller.api.users.service.AccountService;

@RestController
@RequiredArgsConstructor
public class UsersController {

  private final AccountService accountService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ResultDto> signup(@RequestBody AccountDto accountDto) {
    ResultDto resultDto = accountService.createUser(accountDto);
    return ResponseEntity.ok().body(resultDto);
  }

  @GetMapping("/mypage")
  @ResponseStatus(HttpStatus.OK)
  public AccountDto mypage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = (String) authentication.getPrincipal();
    return accountService.getAccountByEmail(email);
  }

}
