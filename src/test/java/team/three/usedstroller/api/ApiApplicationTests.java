package team.three.usedstroller.api;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.three.usedstroller.api.dto.GeoCodingAPi;

@SpringBootTest
@ActiveProfiles("local")
class ApiApplicationTests {
  @Test
  void contextLoads() {
  }

  @Test
  @DisplayName("geoCodingApiTest")
  void geoCodingApiTest(){
    RestTemplate restTemplate = new RestTemplate();
    String lat = "37.6569856";
    String lon = "126.681088";
    String apiToken="IlUI1OV1J+Yofs0ib5q3hg==2FkpjYRP8WMVcXmz";
    URI uri = UriComponentsBuilder
        .fromUriString("https://api.api-ninjas.com")
        .path("/v1/reversegeocoding")
        .queryParam("lat",lat)
        .queryParam("lon",lon)
        .build()
        .toUri();
    System.out.println("uri = " + uri);

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-Api-Key",apiToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<GeoCodingAPi> response = restTemplate.exchange(uri, HttpMethod.GET,entity,GeoCodingAPi.class);
    System.out.println("response = " + response);
  }

}
