package team.three.usedstroller.api.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import team.three.usedstroller.api.common.utils.EntityUtils;
import team.three.usedstroller.api.enums.WeightKeyword;
import team.three.usedstroller.api.error.ApiErrorCode;
import team.three.usedstroller.api.error.ApiException;
import team.three.usedstroller.api.gpt.dto.CacheReqDto;
import team.three.usedstroller.api.gpt.dto.GptMessage;
import team.three.usedstroller.api.gpt.dto.GptRequest;
import team.three.usedstroller.api.gpt.dto.GptResponse;
import team.three.usedstroller.api.gpt.dto.ModelDto;
import team.three.usedstroller.api.gpt.dto.UserInputReqDto;
import team.three.usedstroller.api.gpt.entity.ReviewSummaryEntity;
import team.three.usedstroller.api.gpt.repository.ModelRepository;
import team.three.usedstroller.api.gpt.repository.ModelRepositoryImpl;
import team.three.usedstroller.api.gpt.repository.ReviewSummaryRepository;
import team.three.usedstroller.api.product.domain.Model;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

  private final ModelRepositoryImpl modelRepositoryImpl;
  private final ModelRepository modelRepository;
  private final ReviewSummaryRepository reviewSummaryRepository;
  @Qualifier("gptWebClient")
  private final WebClient gptWebClient;
  private final CacheManager cacheManager;

  @Transactional
  public Flux<String> recommendAndStream(UserInputReqDto req) {
    List<Model> finalCandidates = new ArrayList<>();
    Map<Model,Integer> dbScores = new HashMap<>();
    List<Integer> weightKeywordList = req.getWeightKeywordList();

    // 후보 추리기
    // 1. 하드조건(연령, 가격,쌍둥이)
    List<Model> models = modelRepositoryImpl.filterByHardCondition(req);
    if(models.isEmpty()) {
      throw new ApiException(ApiErrorCode.MODEL_NOT_FOUND);
    }

    // 2-1. 소프트 조건 DB-score
    for(Model model : models) {
      int totalScore = getTotalScore(model, weightKeywordList);
      dbScores.put(model, totalScore);
    }

    List<Model> candidates = dbScores.entrySet()
        .stream()
        .sorted(Map.Entry.<Model, Integer>comparingByValue().reversed())
        .map(Map.Entry::getKey) // 여기에서 Model만 추출
        .limit(3)
        .toList();

    // 1순위 캐시에 저장
    Cache cache = cacheManager.getCache("modelCache");
    cache.put(req.getSessionId(),candidates.get(0).getId());

    // 5. 최종 API 요청
    String apiPrompt = buildPromptRecommend(req, candidates);
    return simulateGptStreaming(apiPrompt,candidates);
  }

  @Transactional
  public Flux<String> recommendAndStreamTest(UserInputReqDto req) {
    String[] lines = {
        "1.",
        "**줄즈 에어플러스**",
        "![줄즈 에어플러스](https://images.",
        "joolz.",
        "com/product.",
        "jpg)",
        "2.",
        "**스토케 요요3**",
        "![스토케 요요3](https://images.",
        "stokke.",
        "com/yoyo3.",
        "jpg)",
        "두 모델 모두 소중한 아이와의 외출을 더욱 편안하고 즐겁게 해줄 수 있는 좋은 선택이지만, 각 모델마다 특징과 장점들이 다르답니다.",
        "저의 추천은 귀한 아이와의 시간을 고려하여, 두 모델을 비교해 설명드릴게요.",
        "### 줄즈 에어플러스",
        "줄즈 에어플러스는 우선 폴딩이 매우 쉬운 점이 큰 장점이에요.",
        "기내 반입도 가능하다는 점에서 여행을 많이 하시는 부모님께 유용하죠.",
        "디자인이 이쁘고 수납공간이 넉넉하여 실용적이에요.",
        "무게가 가벼워서 이동하시기에도 좋습니다.",
        "하지만, 색상 선택의 제약과 범퍼바 사용 시 덜 예쁜 포인트가 있어 아쉬움이 있습니다.",
        "또한, 차량 아래 수납공간이 좁아 불편할 수 있지만, 전체적인 안정성과 기능성 면에서 우수한 평가를 받고 있습니다.",
        "### 스토케 요요3",
        "스토케 요요3은 다양한 컬러 옵션으로 소중한 시간을 색감적으로도 즐겁게 만들어 준답니다.",
        "특히, 초경량 유모차로 휴대성이 뛰어나고, 업그레이드된 기능들이 많아요.",
        "길어진 차양막은 아가를 쾌적하게 보호해 주고, 뒷면 주머니가 있어 소지품 수납에 유용합니다.",
        "단점으로는 높은 가격과 범퍼바가 없다는 점이 있지만, 안전벨트로 이를 보완할 수 있습니다.",
        "두 모델 모두 기내 반입이 가능하다는 점에서 여행 중에도 매우 편리하세요.",
        "또한 쉽게 폴딩이 가능하여 대중교통 이용 시나 차 트렁크에 실을 때도 부담이 없어요.",
        "가격 측면에서는 줄즈 에어플러스가 예산에 더 적합해 보입니다.",
        "최종적으로 추천드리는 모델은 **줄즈 에어플러스**입니다.",
        "이 모델은 현재 사용자님의 예산에 더 맞고 폴딩이 간편하면서도 가볍고 안정적인 구조로 아가와의 외출 시 더 신뢰감 있게 사용할 수 있습니다.",
        "기내 반입성이 좋아 여행 시에도 손쉽게 아이와 소중한 시간을 보낼 수 있어요.",
        "아가와 행복한 외출 시간을 즐기며, 즐거운 육아 생활 이어가시길 바랍니다."
    };

    return Flux.fromArray(lines)
        .delayElements(Duration.ofMillis(400))
        .doOnNext(chunk -> log.info("🔸 응답 전송: {}", chunk));
  }

  private static int getTotalScore(Model model, List<Integer> weightKeywordList) {
    int safeScore = weightKeywordList.contains(1) ? model.getSafeScore()*2 : model.getSafeScore();
    int driveScore = weightKeywordList.contains(2) ? model.getDriveScore()*2: model.getDriveScore();
    int brandScore = weightKeywordList.contains(3) ? model.getBrandScore()*2: model.getBrandScore();
    int priceScore = weightKeywordList.contains(4) ? model.getPriceScore()*2: model.getPriceScore();
    int weightScore = weightKeywordList.contains(5) ? model.getWeightScore()*2: model.getWeightScore();
    int flightScore = weightKeywordList.contains(6) ? model.getFlightScore()*2: model.getFlightScore();
	  return safeScore+driveScore+brandScore+priceScore+weightScore+flightScore;
  }


  private Flux<String> simulateGptStreaming(String userPrompt, List<Model> candidates) {
    Map<String,String> modelImageMap = new HashMap<>();
    for (Model model : candidates) {
      String modelName = model.getName();
      String modelIamge = model.getImageUrl();
      modelImageMap.put(modelName,modelIamge);
    }
    Map<String, Object> request = Map.of(
        "model", "gpt-4o",
        "messages", List.of(
            Map.of("role", "user", "content", userPrompt)
        ),
        "stream", false
    );

    return gptWebClient.post()
        .bodyValue(request)
        .retrieve()
        .bodyToMono(String.class)
        .map(response -> {
          try {
            JsonNode json = new ObjectMapper().readTree(response);
            String content = json
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText("");
            return content;
          } catch (Exception e) {
            log.error("응답 파싱 실패", e);
            return "";
          }
        })
        .flatMapMany(fullText -> { // flatMapMany는 Mono 값을 Flux로 변환할때 씀 hello => h,e,l,l,o
          // ① 문장 단위 혹은 줄 단위 분할
          String[] chunks = fullText.split("(?<=\\.|\\n)"); // 마침표나 줄바꿈으로 끊음
          return Flux.fromArray(chunks);
        })
        .map(String::trim) // 앞뒤공백제거
        .filter(chunk -> !chunk.isEmpty())
        .map(chunk -> {
          // "![모델명](이미지 URL)" 형태의 텍스트인지 확인
          if (chunk.matches("!\\[.+]\\(이미지 URL\\)")) {
            // 정규식 그룹을 사용해 괄호 안 모델명 추출 → "SEEC 롤리팝2"
            String full = chunk.replaceAll("!\\[(.+)]\\(이미지 URL\\)", "$1");
            // 모델명을 브랜드와 모델명으로 나눔 → ["SEEC", "롤리팝2"]
            String[] parts = full.split(" ", 2);
            // 모델명만 추출 (브랜드는 버림). ex) "롤리팝2"
            String modelName = parts.length == 2 ? parts[1] : parts[0];
            // 모델명에 대응하는 실제 이미지 URL을 맵에서 가져옴. 없으면 빈 문자열 사용
            String imageUrl = modelImageMap.getOrDefault(modelName, "");
            // "![SEEC 롤리팝2](https://~~)" 형식으로 다시 조립하여 반환
            return "![" + full + "](" + imageUrl + ")";
          }
          return chunk;
        })
        .delayElements(Duration.ofMillis(800)) // ② 스트리밍처럼 보여주기
        .doOnNext(chunk -> log.info("🔸 응답 전송: {}", chunk));
  }

  public String buildPromptRecommend(UserInputReqDto input, List<Model> candidates) {
    StringBuilder sb = new StringBuilder();

    // 1. 시스템 역할 안내
    sb.append("[system 역할 안내]\n");
    sb.append("넌 유모차 전문가야. 사용자 조건과 유모차 모델 정보, 후기 요약을 참고해 최적의 유모차 1개를 추천해줘.\n");
    sb.append("1순위 모델과 2순위 모델을 반드시 모두 비교해줘. 두 모델 각각의 장단점을 비교 분석한 뒤, 어떤 모델이 더 적합한지 최종 추천을 내려줘.\n");
    // sb.append("답변 마지막에 주제와 자연스럽게 이어지는 후속질문 2~3개를 번호로 제시해줘.\n\n");
//    sb.append("- 사용자 답변과 주제 연관성 있는 후속질문 2~3개를 작성한다.\n");
//    sb.append("- 후속질문은 번호를 붙이고, 선택형 문장으로 작성한다.\n");
//    sb.append("- 후속질문은 답변과 자연스럽게 이어질 수 있게 만든다.\n\n");

    // 2. 답변 출력 포맷 안내
    sb.append("[답변 출력 포맷]\n");
    sb.append("1. **모델명**  \n");
    sb.append("   ![모델명](이미지 URL)\n");
    sb.append("2. 추천 이유 설명\n");
    // sb.append("3. 하이퍼링크 텍스트는 절대로 줄을 나누지 말고 한 줄로 출력\n");
    // sb.append("4. [더 많은 중고 유모차 보러가기](https://jungmocha.co.kr)\n\n");


    
    // 3. 사용자 조건

    sb.append("[사용자 조건]\n");
    sb.append("- 아이 개월수: ").append(getAgeLabel(input.getAgeCode())).append("\n");
    sb.append("- 쌍둥이 : ").append(input.getTwin()? "예" : "아니오").append("\n");
    sb.append("- 신제품 최대 가격: ").append(input.getMaxPriceNew()).append("원\n");
    sb.append("- 중고제품 최대 가격: ").append(input.getMaxPriceUsed()).append("원\n");
    sb.append("- 기타 : ").append(input.getUserText()).append("\n\n");

    // 4. 후보 모델 정보
    sb.append("[후보 모델 정보]\n");
    for (int i = 0; i < candidates.size(); i++) {
      List<ReviewSummaryEntity>  reviews = reviewSummaryRepository.findRandom3ByModelId(candidates.get(i).getId());
      String rank = (i == 0) ? "1순위" : "2순위";
      sb.append(rank).append(" - ").append(candidates.get(i).getBrand()).append(" ").append(candidates.get(i).getName()).append("\n");
      sb.append("- 후기 요약:\n");
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
      for(ReviewSummaryEntity review : reviews) {
        String cleaned = cleanSummaryPrefix(review.getSummary());
        sb.append("> ").append(cleaned).append("\n");
      }
    }

    // 5. 답변
    sb.append("[답변]\n");
    sb.append("위 두 모델은 각각 어떤 장단점이 있는지 설명해줘.\n");
    sb.append("두 모델을 비교한 후, 사용자 조건에 가장 적합한 유모차 1개를 최종 추천해줘.\n");
    sb.append("사용자 조건 중 '기타'에 들어갔던 내용도 언급을 해줘.\n");
    sb.append("두 모델 모두 꼭 언급해주고, 비교 설명은 상세히 해줘.\n");
    sb.append("- 어머님 타겟의 따뜻하고 신뢰감 있는 말투로.\n");
    sb.append("- 문장은 부드럽지만 신뢰감 있게, 실제 사용 상황을 떠올리게 설명해줘.\n");
    sb.append("- 감성 키워드 '소중한 시간', '아가와의 외출'을 포함해줘.\n");

    return sb.toString();
  }

  private String getAgeLabel(String age) {
    String ageLabel;
    switch (age) {
      case "s":
        ageLabel = "0~6개월";
        break;
      case "m":
        ageLabel = "7~12개월";
        break;
      case "l":
        ageLabel = "13개월 이상";
        break;
      default:
        ageLabel = "알 수 없음";
    }
    return ageLabel;
  }

  private String cleanSummaryPrefix(String summary) {
    // 앞에 오는 숫자 + 점("2.") 또는 ** 제거
    return summary.replaceFirst("^(\\d+\\.\\s*|\\*\\*\\s*)", "").trim();
  }

  public void saveCache(CacheReqDto req) {
    cacheManager.getCache("modelCache").put(req.getSessionId(),20L);
  }


  public Long getCache(String sessionId) {
    Cache cache = cacheManager.getCache("modelCache");
    if( cache == null ) return null;
    Long modelId = cache.get(sessionId, Long.class);
    cache.evict(sessionId);
    return modelId;
  }

  public ModelDto getModelInfo(String sessionId) {
    // 캐시에서 모델 id 가져오기, key는 sessionId, value = modelId
    Cache cache = cacheManager.getCache("modelCache");
    if( cache == null ) return null;
    Long modelId = cache.get(sessionId, Long.class);
    // 모델조회
    Model model = EntityUtils.findOrThrow(modelRepository.findById(modelId), ApiErrorCode.MODEL_NOT_FOUND);
    cache.evict(sessionId);
    return ModelDto.of(model);
  }


  // ============================================안쓰는 부분 =========================================================


  // 2-2. 소프트 조건 gpt-score, 하드조건에 뽑힌 모델, 유저 가중치 기반으로 점수 50점만점 , 생략 => GPT 부분
  // for (Model model : models) {
  //   int score = 0;
  //   score = getScore(req, model, score);
  //   modelScores.put(model,score);
  // }
  //
  // log.info("modelScores: {}", modelScores);
  //
  // // 3. 점수 순 정렬(3개)
  // List<Model> gptCandidates = modelScores.entrySet()
  //     .stream()
  //     .sorted(Map.Entry.<Model, Integer>comparingByValue().reversed())
  //     .map(Map.Entry::getKey) // 여기에서 Model만 추출
  //     .collect(Collectors.toList());
  //
  // log.info("sortedList: {}", gptCandidates);



  private int getScore(UserInputReqDto req, Model model, int score) {
    // 기타요청 정확도 점수화
    // 1. 프롬프트 작성
    String userPrompt = scorePrompt(model.getId(), req.getUserText(), req.getWeightKeywordList());
    // 2. api 요청
    String res = callGptApi(userPrompt);
    score = score + Integer.parseInt(res.replaceAll("점", "").replaceAll("\\.", ""));
    return score;
  }

  public String scorePrompt(Long modelId, String userText,List<Integer> weightKeywordList) {
    // 후기 리스트 가져오기
    List<ReviewSummaryEntity> reviewList = reviewSummaryRepository.findRandom3ByModelId(modelId);


    StringBuilder sb = new StringBuilder();

    sb.append("당신은 유모차를 추천하기 위해, 사용자의 요구사항과 제품 후기를 비교하여 점수를 매기는 도우미입니다.\n\n");

    // 사용자 요청
    sb.append("[사용자 요청사항]\n");
    sb.append("\"").append(userText).append("\"\n\n");

    // 중요 항목 (가중치 요소)

    sb.append("[사용자가 중요하게 여기는 항목]\n");
    for (Integer code : weightKeywordList) {
      String keyword = WeightKeyword.labelOf(code);
      sb.append("- ").append(keyword).append("\n");
    }
    sb.append("\n");

    // 후기들
    for (int i = 0; i < reviewList.size(); i++) {
      sb.append("[후기 ").append(i + 1).append("]\n");
      sb.append("\"").append(reviewList.get(i)).append("\"\n\n");
    }

    sb.append("---\n\n");

    sb.append("각 후기의 내용을 참고하여,\n");
    sb.append("**사용자 요청사항과 후기들이 얼마나 잘 일치하는지**, 특히 '중요하게 여기는 항목'과 얼마나 잘 맞는지를 중심으로 평가해 주세요.\n\n");
    sb.append("- 점수는 0점에서 50점 사이로 매겨주세요.\n");
    sb.append("- 일치하는 내용이 많을수록 높은 점수를 주세요.\n");
    sb.append("- **설명 없이 숫자 하나만 출력해 주세요.**\n");
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
}
