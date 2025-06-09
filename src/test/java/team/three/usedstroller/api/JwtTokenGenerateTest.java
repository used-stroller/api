package team.three.usedstroller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import team.three.usedstroller.api.common.jwt.JwtTokenProvider;
import team.three.usedstroller.api.users.dto.ResponseLoginTokenDto;
import team.three.usedstroller.api.users.entity.Account;
import team.three.usedstroller.api.users.repository.AccountRepository;

@org.springframework.boot.test.context.SpringBootTest
@ActiveProfiles("prod")
@TestPropertySource(properties = {
    "EXTERNAL_API_KEY=이런 환경변수 넣어야 동작함"
})
public class JwtTokenGenerateTest {

  @Autowired
  private  JwtTokenProvider jwtTokenProvider;

  @Autowired
  private  AccountRepository accountRepository;

	@Test
  void generateJwtToken(){
      Account account = accountRepository.findById(23L).orElse(null);
      ResponseLoginTokenDto responseLoginTokenDto = jwtTokenProvider.generateTokenDto(account);
      System.out.println("responseLoginTokenDto = " + responseLoginTokenDto.getAccessToken());
    }

}
