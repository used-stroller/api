package team.three.usedstroller.api;

<<<<<<< HEAD
import org.junit.jupiter.api.Test;


class UnitTest {

  @Test
  void tenOrEleven (){
    class Static{
      public int a =20;
      static int b= 0;
    }
    int a;
    a=10;
    Static.b = a;
    Static st = new Static();
    System.out.println(Static.b++);
=======
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

import team.three.usedstroller.api.gpt.dto.OpenAiReqDto;

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

  @Test
  @DisplayName("map,map 더하기")
  void compareMaps() {
    Map<String, Integer> mapA = new HashMap<>();
    mapA.put("A",2);
    mapA.put("B",1);
    mapA.put("C",3);
    Map<String, Integer> mapB = new HashMap<>();
    mapB.put("A",2);
    mapB.put("B",1);
    mapB.put("C",3);

    // key
    Set<String> allKeys = new HashSet<>();
    allKeys.addAll(mapA.keySet());
    allKeys.addAll(mapB.keySet());

    // 합계
    Map<String, Integer> combined = new HashMap<>();
    for (String key : allKeys) {
      int a = mapA.get(key);
      int b = mapB.get(key);
      combined.put(key,a+b);
    }
    List<Map.Entry<String,Integer>> top3 = combined.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())// 오름차순
        .limit(3)
        .toList();

    for (Map.Entry<String, Integer> e : top3) {
      System.out.println("e = " + e.getKey());
    }
>>>>>>> main
  }
}
