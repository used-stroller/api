package team.three.usedstroller.api.chat.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryResDto {
	String currentUserId;
	String receiverId;
	List<ChatMessageDto> chatMessages;
}
