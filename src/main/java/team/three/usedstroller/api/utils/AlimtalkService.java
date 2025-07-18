package team.three.usedstroller.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpMethod;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team.three.usedstroller.api.rental.dto.AlimTalkDto;
import team.three.usedstroller.api.rental.dto.RentalRequestDto;
import team.three.usedstroller.api.rental.entity.RentalContractEntity;
import team.three.usedstroller.api.rental.entity.RentalEntity;
import team.three.usedstroller.api.rental.repository.RentalContractRepository;
import team.three.usedstroller.api.rental.repository.RentalContractRepositoryImpl;

/**
 * nhncloud 알림톡 API v2.3 가이드
 * https://docs.nhncloud.com/ko/Notification/KakaoTalk%20Bizmessage/ko/alimtalk-api-guide/
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class AlimtalkService {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RentalContractRepository rentalContractRepository;

    private static final String APPKEY        = "hY5p84S3evU26drJ";
    private static final String SECURE_KEY    = "rPJDFJd8X3cQbS8tOG00vMkbUxrX2LZ8";
    private static final String SENDER_KEY    = "af35ca0ba49474dad0a127fec17ceff255131711";
    private static final String TEMPLATE_CODE = "apply";
    private static final String SEND_API_URL  = "https://api-alimtalk.cloud.toast.com/alimtalk/v2.3/appkeys/" + APPKEY + "/messages";

	private static final List<String> DEVELOPERS = List.of(
            "01029376030"  // lee
    );
	private final RentalContractRepositoryImpl rentalContractRepositoryImpl;

	public boolean sendAlimtalk(AlimTalkDto request) {
		try{
			RentalContractEntity contract = rentalContractRepositoryImpl.getRentalDetails();
			List<String> phoneNumbers = getPhoneNumbers(contractEntity);
			Map<String ,Object> templateParameter = createTemplateParameter(request);
			log.info("알림톡 템플릿 파라미터:\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(templateParameter)); //Json형식의 string으로 변환해주고 들여쓰기 해서 이쁘게 출력

			List<Map<String, Object>> recipientList = new ArrayList<>();
			for(String phoneNumber : phoneNumbers){
				recipientList.add(Map.of(
					"recipientNo", phoneNumber,
					"templateParameter", templateParameter
				));
			}

			// 전송 body
			Map<String, Object> body = new HashMap<>();
			body.put("senderKey", SENDER_KEY);
			body.put("templateCode",TEMPLATE_CODE);
			body.put("recipientList", recipientList);
			log.info("messages REQ body:\n{}",objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));

			// header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Security-Key", SECURE_KEY);


			// API 호출
			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(
				SEND_API_URL,
				HttpMethod.POST,
				entity,
				String.class
			);

			log.info("### sendAlimtalk");

			if(response.getStatusCode().is2xxSuccessful()){
				String responseBody = response.getBody();
				JsonNode jsonNode = objectMapper.readTree(responseBody);
				log.info("messages API 응답 수신:\n{}", jsonNode.toPrettyString());
				return jsonNode.path("header").path("isSuccessful").asBoolean(false);
			} else {
				log.warn("messages API 응답 실패: {}", response.getStatusCode());
				return false;
			}
		} catch (Exception e) {
            log.error("messages API 예외 발생", e);
            return false;
        }
	}



	private Map<String, Object> createTemplateParameter(RentalRequestDto request) {
		Map<String, Object> params = new HashMap<>();
		params.put("금액",request.getRentalPrice());
		return params;
	}

	private List<String> getPhoneNumbers(RentalContractEntity contractEntity) {
		Set<String> phoneNumbers = new LinkedHashSet<>(); // 중복 제거 + 순서 유지
		String customerPhone = contractEntity.getPhone();
		phoneNumbers.addAll(DEVELOPERS);
		phoneNumbers.add(customerPhone);
		return new ArrayList<>(phoneNumbers);
	}

}
