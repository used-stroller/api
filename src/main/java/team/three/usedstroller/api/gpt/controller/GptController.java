package team.three.usedstroller.api.gpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
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

}
