package team.three.usedstroller.api;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

public class UnitTest {

  @Test
  void DateFormatter(){
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
    String date = now.format(formatter);
    System.out.println("date = " + date);
    
  }

  @Test
  void base64() {
    String plainText = "jungmocha3jo";
    String base64Encoded = Base64.getEncoder().encodeToString(plainText.getBytes());
    System.out.println("base64Encoded = " + base64Encoded);
    
    byte[] decodeBytes = Base64.getDecoder().decode(base64Encoded);
    String decodedString = new String(decodeBytes);
    System.out.println("decodedString = " + decodedString);
    
  }

  @Test
  void generateSecretKey() throws NoSuchAlgorithmException {
    // HMAC-SHA512에 적합한 SecretKey 생성 (512비트)
    KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA512");
    keyGenerator.init(512); // 키 크기 설정
    SecretKey secretKey = keyGenerator.generateKey();

    // Base64 인코딩
    String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    System.out.println("Generated Base64 Secret Key: " + base64EncodedKey);
  }
}
