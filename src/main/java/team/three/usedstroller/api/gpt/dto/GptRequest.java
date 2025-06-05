package team.three.usedstroller.api.gpt.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptRequest {
	private String model; //gpt 모델
	private List<GptMessage> messages;
}

