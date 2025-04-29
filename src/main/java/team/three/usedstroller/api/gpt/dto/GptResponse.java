package team.three.usedstroller.api.gpt.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptResponse {
	private List<Choice> choices;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Choice {
		private GptMessage message;
	}
}
