package team.three.usedstroller.api.gpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration

public class WebClientConfig {
  @Bean
  public WebClient gptWebClient(@Value("${gpt.api.key}") String apiKey) {
    return WebClient.builder()
        .baseUrl("https://api.openai.com/v1/chat/completions")
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .build();
  }
}
