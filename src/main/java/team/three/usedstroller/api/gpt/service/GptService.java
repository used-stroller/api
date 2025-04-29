package team.three.usedstroller.api.gpt.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.three.usedstroller.api.gpt.dto.UserInputReqDto;
import team.three.usedstroller.api.gpt.repository.ModelRepositoryImpl;
import team.three.usedstroller.api.product.domain.Model;

@Service
@RequiredArgsConstructor
public class GptService {

  private final ModelRepositoryImpl modelRepositoryImpl;

  @Transactional
  public void recommend(UserInputReqDto req) {
    
    // 후보 추리기
    // 1. 하드조건(연령, 가격, 유모차 타입,쌍둥이)
    List<Model> models = modelRepositoryImpl.filterByHardCondition(req);
    Map<Model,Integer> modelScores = new HashMap<>();
    // 2. 소프트 조건(무게타입,기내반입, 유저 텍스트) 25점, 25점, 50점
    for (Model model : models) {
      int score = 0;
      // 무게선호도
      if (model.getWeightType().equals(req.getWeightType().toString())) {
          score = score+25;
      }

      // 기내반입선호도
      if (model.getCarryOn().equals(req.getCarryOn())) {
          score = score+25;
      }
      // 기타요청 정확도


      modelScores.put(model, score);
    }

    // 3. 부가정보 조회



    // 4. api 요청



    // 추천
    String prompt = buildPrompt();


  }

  public String buildUserTextPrompt(Long modelId, String userText) {
    // 후기 리스트
    getReview();


    StringBuilder sb = new StringBuilder();
    sb.append()
  }

  
  public String buildPrompt(UserInputReqDto req) {
    StringBuilder sb = new StringBuilder();
    // 1. 시스템 역할 안내
    sb.append("[system 역할 안내]\n");
    sb.append("넌 유모차 전문가야. 사용자 조건과 유모차 모델 정보, 후기 요약을 참고해 가장 적합한 유모차를 1개 추천해줘. 추천 이유도 함께 설명해줘. 그리고 모델에 맞는 내가 첨부한 이미지url도 같이 보여줘.\n");
    sb.append("답변이 끝나면 다음을 추가해:\n");
    sb.append("- 사용자 답변과 주제 연관성 있는 후속질문 2~3개를 작성한다.\n");
    sb.append("- 후속질문은 번호를 붙이고, 선택형 문장으로 작성한다.\n");
    sb.append("- 후속질문은 답변과 자연스럽게 이어질 수 있게 만든다.\n\n");
    sb.append("[답변 출력 포맷]\n");
    sb.append("1. 추천 유모차 및 이유 설명\n");
    sb.append("2. 추천 이미지 URL 출력\n");
    sb.append("3. 후속질문 2~3개 제시 (번호 붙여서)\n\n");

    // 2. 사용자 조건
    sb.append("[사용자 조건]\n");
    sb.append("- 아이 나이: ").append(input.getBabyAge()).append("개월\n");
    sb.append("- 차량: ").append(input.getCarType()).append("\n");
    sb.append("- 신제품 가격: ").append(input.getBudget()).append("만원\n");
    sb.append("- 접기 쉬운 모델 선호: ").append(input.isFoldEasy() ? "예" : "아니오").append("\n");
    // 기타 요청이 있다면 추가
    sb.append("- 기타 요청: 예쁜 유모차, 디자인 예쁜 유모차\n\n");

    // 3. 후보 모델 정보
    sb.append("[후보 모델 정보]\n");
    int index = 1;
    for (StrollerModel model : candidates) {
      sb.append(index++).append(". ").append(model.getBrand()).append(" ").append(model.getName()).append(" (").append(model.getBrand()).append(")\n");
      sb.append("- 무게: ").append(model.getWeight()).append("kg\n");
      sb.append("- 폴딩 방식: ").append(model.getFoldingType()).append("\n");
      sb.append("- 접기 쉬움: ").append(model.isFoldEasy() ? "true" : "false").append("\n");
      sb.append("- 신제품 가격: ").append(model.getNewPrice()).append("만원\n");
      sb.append("- 중고 가격: ").append(model.getUsedPrice()).append("만원\n");
      sb.append("- 신생아 사용 가능: ").append(model.isNewBorn() ? "true" : "false").append("\n");
      sb.append("- 이미지: ").append(model.getImageUrl()).append("\n");
      sb.append("- 후기 요약:\n").append("\"").append(model.getReviewSummary()).append("\"\n");
    }

    // 4. 질문
    sb.append("\n[질문]\n");
    sb.append("위 조건에 가장 적합한 유모차를 추천해줘. 이유도 함께 설명해줘.\n");
    sb.append("- 답변할 때 30~40대 어머님들이 좋아할 따뜻하고 친근한 말투로 작성하라.\n");
    sb.append("- 문장은 부드럽지만 신뢰감 있게, 실제 사용 상황을 떠올리게 설명하라.\n");
    sb.append("- 감성 키워드(소중한 시간, 아가와의 외출 등)를 자연스럽게 포함하라.\n");

    return sb.toString();
  }
}
