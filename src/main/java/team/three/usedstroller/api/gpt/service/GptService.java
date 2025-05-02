package team.three.usedstroller.api.gpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import team.three.usedstroller.api.enums.StrollerType;
import team.three.usedstroller.api.enums.WeightType;
import team.three.usedstroller.api.gpt.dto.GptMessage;
import team.three.usedstroller.api.gpt.dto.GptRequest;
import team.three.usedstroller.api.gpt.dto.GptResponse;
import team.three.usedstroller.api.gpt.dto.OpenAiReqDto;
import team.three.usedstroller.api.gpt.dto.UserInputReqDto;
import team.three.usedstroller.api.gpt.entity.ReviewSummaryEntity;
import team.three.usedstroller.api.gpt.repository.ModelRepositoryImpl;
import team.three.usedstroller.api.gpt.repository.ReviewSummaryRepository;
import team.three.usedstroller.api.product.domain.Model;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

  private final ModelRepositoryImpl modelRepositoryImpl;
  private final ReviewSummaryRepository reviewSummaryRepository;
  @Qualifier("gptWebClient")
  private final WebClient gptWebClient;

  @Transactional
  public Flux<String> recommendAndStream(UserInputReqDto req) {
    
    // 후보 추리기
    // 1. 하드조건(연령, 가격, 유모차 타입,쌍둥이)
    List<Model> models = modelRepositoryImpl.filterByHardCondition(req);
    Map<Model,Integer> modelScores = new HashMap<>();
    
    // 2. 소프트 조건(무게타입,기내반입, 유저 텍스트) 25점, 25점, 50점
    for (Model model : models) {
      int score = 0;
      // 무게선호도
      if (model.getWeightType().equals(req.getWeightType().toString())) {
          score = score+5;
      }
      // 기내반입선호도
      if (model.getCarryOn().equals(req.getCarryOn())) {
          score = score+5;
      }
      // 기타요청 정확도 점수화
      // 1. 프롬프트 작성
      String userPrompt = buildPromptUserText(model.getId(), req.getUserText());
      // 2. api 요청
      String res = callGptApi(userPrompt);
      score = score + Integer.parseInt(res.replaceAll("점", "").replaceAll("\\.", ""));
      // 3. 모델과 점수 put
      modelScores.put(model,score);
    }
    log.info("modelScores: {}", modelScores);

    // 4. 점수 순 정렬(3개)
    List<Model> candidates = modelScores.entrySet()
        .stream()
        .sorted(Map.Entry.<Model, Integer>comparingByValue().reversed())
        .limit(3)
        .map(Map.Entry::getKey) // ✅ 여기에서 Model만 추출
        .collect(Collectors.toList());

    log.info("sortedList: {}", candidates);

    // 5. 프롬프트 작성
    String apiPrompt = buildPromptRecommend(req, candidates);
    log.info("apiPrompt: {}", apiPrompt);

    // 6. 최종 API 요청
    return streamGptApi(apiPrompt);
  }

  public String buildPromptUserText(Long modelId, String userText) {
    // 후기 리스트 가져오기
    List<ReviewSummaryEntity> reviewList = reviewSummaryRepository.findRandom3ByModelId(modelId);


    StringBuilder sb = new StringBuilder();
    sb.append("당신은 유모차를 추천하기 위해, 사용자의 요구사항과 제품 후기를 비교하여 점수를 매기는 도우미입니다.\n\n");

    // 사용자 요청
    sb.append("[사용자 요청사항]\n");
    sb.append("\"").append(userText).append("\"\n\n");

    // 후기들 추가
    for (int i = 0; i <reviewList.size(); i ++ ) {
      sb.append("[후기 ").append(i + 1).append("]\n");
      sb.append("\"").append(reviewList.get(i)).append("\"\n\n");
    }
    sb.append("---\n\n");
    sb.append("각 후기의 내용을 참고하여,\n");
    sb.append("**사용자 요청사항과 후기들이 얼마나 잘 일치하는지** 평가해 주세요.\n\n");
    sb.append("- 총점은 0점에서 50점 사이로 매겨주세요.\n");
    sb.append("- 일치하는 내용이 많을수록 높은 점수를 주세요.\n");
    sb.append("- **아무 설명도 하지 말고, 오직 숫자 하나만 출력해.**\n");
    return sb.toString();
  }

  public String callGptApi(String userPrompt) {
    GptRequest request = new GptRequest(
        "gpt-3.5-turbo",
        List.of(
            new GptMessage("user", userPrompt)
        )
    );

    GptResponse response = gptWebClient.post()
        .bodyValue(request)
        .retrieve()
        .onStatus(HttpStatusCode::isError, res ->
            res.bodyToMono(String.class).flatMap(errorBody -> {
              log.info("에러바디 :" + errorBody);
              return Mono.error(new RuntimeException("상태코드 :" + res.statusCode()));
            })
        )
        .bodyToMono(GptResponse.class)
        .block();

    return response.getChoices().get(0).getMessage().getContent();
  }

  public Flux<String> streamGptApi(String userPrompt) {
    Map<String, Object> request = Map.of(
        "model","gpt-3.5-turbo",
        "messages",List.of(
            Map.of(
                "role","user",
                "content", userPrompt
            )
        ),
        "stream",true
    );

    return gptWebClient.post()
        .bodyValue(request)
        .retrieve()
        .onStatus(HttpStatusCode::isError, res ->
            res.bodyToMono(String.class).flatMap(errorBody -> {
              log.warn("GPT API 에러 바디: {}", errorBody);
              return Mono.error(new RuntimeException("상태코드: " + res.statusCode()));
            })
        )
        .bodyToFlux(String.class)
        .flatMap(line -> Flux.fromArray(line.split("\n"))) // 여러 줄로 온 경우 분리
        .filter(line -> line.startsWith("data: ")) // "data: " 로 시작하는 줄만 추출
        .map(line -> line.substring("data: ".length())) // 앞 prefix 제거
        .takeWhile(data -> !data.equals("[DONE]"))      // 끝 표시 제거
        .map(data -> {
          try {
            JsonNode json = new ObjectMapper().readTree(data);
            return json
                .get("choices")
                .get(0)
                .get("delta")
                .get("content")
                .asText("");
          } catch (Exception e) {
            log.error("GPT 스트림 파싱 오류: {}", data, e);
            return "";
          }
        })
        .filter(text -> !text.isBlank());
  }





  public String buildPromptRecommend(UserInputReqDto input, List<Model> candidates) {
    StringBuilder sb = new StringBuilder();

    // 1. 시스템 역할 안내
    sb.append("[system 역할 안내]\n");
    sb.append("넌 유모차 전문가야. 사용자 조건과 유모차 모델 정보, 후기 요약을 참고해 가장 적합한 유모차를 1개 추천해줘. 추천 이유도 함께 설명해줘. 그리고 모델에 맞는 내가 첨부한 이미지url도 같이 보여줘.\n");
    sb.append("답변이 끝나면 다음을 추가해:\n");
    sb.append("- 사용자 답변과 주제 연관성 있는 후속질문 2~3개를 작성한다.\n");
    sb.append("- 후속질문은 번호를 붙이고, 선택형 문장으로 작성한다.\n");
    sb.append("- 후속질문은 답변과 자연스럽게 이어질 수 있게 만든다.\n\n");

    // 2. 답변 출력 포맷 안내
    sb.append("[답변 출력 포맷]\n");
    sb.append("1. \n");
    sb.append("**모델명**  \n");
    sb.append("![모델명](선택된 모델 이미지 URL) 형식으로 삽입\n");
    sb.append("2. 추천 유모차 및 이유 설명\n");
    sb.append("3. 후속질문 2~3개 제시 (번호 붙여서)\n");
    sb.append("4. [더 많은 중고 유모차를 보고 싶다면 클릭](https://jungmocha.co.kr)\n\n");

    
    // 3. 사용자 조건
    sb.append("[사용자 조건]\n");
    sb.append("- 아이 개월수: ").append(input.getAge()).append("개월\n");
    sb.append("- 쌍둥이 여부: ").append(input.getTwin()? "예" : "아니오").append("\n");
    sb.append("- 신제품 최대 가격: ").append(input.getMaxPriceNew()).append("원\n");
    sb.append("- 중고제품 최대 가격: ").append(input.getMaxPriceUsed()).append("원\n");
    sb.append("- 유모차 타입: ").append(input.getType()).append("\n");
    sb.append("- 유모차 무게: ").append(input.getWeightType()).append("\n");
    sb.append("- 기내반입 여부: ").append(input.getCarryOn() ? "예" : "아니오").append("\n");
    sb.append("- 기타 요청: ").append(input.getUserText()).append("\n\n");

    // 4. 후보 모델 정보
    sb.append("[후보 모델 정보]\n");
    int index = 1;
    for (Model model : candidates) {
      List<ReviewSummaryEntity>  reviews = reviewSummaryRepository.findRandom3ByModelId(model.getId());

      // sb.append(index++).append(". ").append(model.getBrand()).append(" ").append(model.getName()).append(" (").append(model.getBrand()).append(")\n");
      // sb.append("- 유모차 타입: ").append(model.getStrollerType()).append("\n");
      // sb.append("- 출시년도: ").append(model.getLaunched()).append("년\n");
      // sb.append("- 제조: ").append(model.getCountry()).append("\n");
      // sb.append("- 신제품 가격: ").append(model.getNewPrice()).append("원\n");
      // sb.append("- 중고 가격: ").append(model.getUsedPrice()).append("원\n");
      // sb.append("- 무게: ").append(model.getWeight()).append("kg\n");
      // sb.append("- 무게 타입: ").append(model.getWeightType()).append("\n");
      // sb.append("- 사이즈: ").append(model.getSize()).append("cm\n");
      // sb.append("- 등받이 조절: ").append(model.getReclining()).append("(각도)조절\n");
      // sb.append("- 기내반입 여부: ").append(model.getCarryOn()).append("\n");
      // sb.append("- 쌍둥이 여부: ").append(model.getTwin()).append("\n");
      // sb.append("- 이미지: ").append(model.getImageUrl()).append("\n");
      sb.append("- 후기 요약:\n");
      for(ReviewSummaryEntity review : reviews) {
        String cleaned = cleanSummaryPrefix(review.getSummary());
        sb.append("> ").append(cleaned).append("\n");
      }
    }

    // 5. 질문
    sb.append("[질문]\n");
    sb.append("위 조건에 가장 적합한 유모차를 추천해줘. 이유도 함께 설명해줘.\n");
    sb.append("- 답변할 때 30~40대 어머님들이 좋아할 따뜻하고 친근한 말투로 작성하라.\n");
    sb.append("- 문장은 부드럽지만 신뢰감 있게, 실제 사용 상황을 떠올리게 설명하라.\n");
    sb.append("- 감성 키워드(소중한 시간, 아가와의 외출 등)를 자연스럽게 포함하라.\n");

    return sb.toString();
  }

  private String cleanSummaryPrefix(String summary) {
    // 앞에 오는 숫자 + 점("2.") 또는 ** 제거
    return summary.replaceFirst("^(\\d+\\.\\s*|\\*\\*\\s*)", "").trim();
  }
}
