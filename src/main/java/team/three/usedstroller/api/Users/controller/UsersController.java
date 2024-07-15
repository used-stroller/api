package team.three.usedstroller.api.Users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.Users.domain.Account;
import team.three.usedstroller.api.Users.domain.dto.AccountDto;
import team.three.usedstroller.api.Users.service.AccountService;

@RestController
@RequiredArgsConstructor
public class UsersController {

  private final AccountService accountService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public AccountDto signup(@RequestBody AccountDto accountDto) {

    Account user = accountService.createUser(accountDto);
    return AccountDto.of(user);
  }

}
