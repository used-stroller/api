package team.three.usedstroller.api;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import lombok.RequiredArgsConstructor;
import team.three.usedstroller.api.common.jwt.JwtTokenProvider;
import team.three.usedstroller.api.users.dto.ResponseLoginTokenDto;
import team.three.usedstroller.api.users.entity.Account;
import team.three.usedstroller.api.users.repository.AccountRepository;

@ActiveProfiles(value = "prod")
@TestPropertySource(properties = {
})
@org.springframework.boot.test.context.SpringBootTest
public class SpringBootTest {

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
