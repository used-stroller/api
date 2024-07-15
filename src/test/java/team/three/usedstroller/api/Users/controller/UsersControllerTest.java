package team.three.usedstroller.api.Users.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.three.usedstroller.api.Users.domain.Account;
import team.three.usedstroller.api.Users.domain.dto.AccountDto;
import team.three.usedstroller.api.Users.domain.dto.Authority;
import team.three.usedstroller.api.Users.repository.AccountRepository;
import team.three.usedstroller.api.Users.service.AccountService;
import team.three.usedstroller.api.util.WebIntegrationTest;

class UsersControllerTest extends WebIntegrationTest {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @BeforeEach
  void init() {
    accountRepository.deleteAll();
  }

  @Test
  void create_user() {
    //given
    AccountDto accountDto = new AccountDto("test", "1111");

    //when
    Account user = accountService.createUser(accountDto);

    //then
    assertThat(user.getId()).isNotNull();
    assertThat(user.getEmail()).isEqualTo(accountDto.getEmail());
    assertThat(user.getRole()).isEqualTo(Authority.ROLE_USER);
  }
}