package team.three.usedstroller.api.gpt.controller;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import team.three.usedstroller.api.gpt.dto.CacheReqDto;
import team.three.usedstroller.api.gpt.dto.UserInputReqDto;
import team.three.usedstroller.api.gpt.service.GptService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GptController {

  private final GptService gptService;

  @PostMapping(value = "/recommend", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//  @PostMapping(value = "/recommend")
  public Flux<String> recommendStroller(@RequestBody UserInputReqDto req) {
      return gptService.recommendAndStream(req);
  }

  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> streamMessages() {
    return Flux.interval(Duration.ofSeconds(1))
        .take(5)
        .map(i -> "메시지 " + i);
  }

  @PostMapping("/test/save/cache")
  public void cacheTest(@RequestBody CacheReqDto req){
    gptService.saveCache(req);
  }

  @GetMapping("/test/get/cache")
  public Long getCache(String sessionId){
    return gptService.getCache(sessionId);
  }

  @GetMapping("/test/has/cache")
  public boolean hasCache(String sessionId){
    return gptService.hasCache(sessionId);
  }

}
