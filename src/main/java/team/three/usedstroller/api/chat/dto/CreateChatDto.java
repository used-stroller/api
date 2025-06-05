package team.three.usedstroller.api.chat.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CreateChatDto {
	List<String> userIds;
	Long productId;
}
