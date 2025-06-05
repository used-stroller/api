package team.three.usedstroller.api.common.utils;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.three.usedstroller.api.product.dto.GeoCodingAPi;

@Slf4j
@RequiredArgsConstructor
public class ReverseGeocodingApi {
  private final RestTemplate restTemplate;
  private final Environment environment;
  private String apiToken;

  @PostConstruct
  public void init() {
    this.apiToken = environment.getProperty("apiNinjas.token");
  }

  public ResponseEntity getCityNameByLotAndLon(String lat, String lon) {
    URI uri = UriComponentsBuilder
        .fromUriString("https://api.api-ninjas.com")
        .path("/v1/reversegeocoding")
        .queryParam("lat",lat)
        .queryParam("lon",lon)
        .build()
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("x-api-key",apiToken);

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<GeoCodingAPi>  response = restTemplate.exchange(uri,HttpMethod.GET,entity,GeoCodingAPi.class);
    return response;
  }
}
