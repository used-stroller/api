package team.three.usedstroller.api.chat.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomDto {
	private String roomId; // 123_1_3
	private String opponentName;
	private String productImageUrl;
	private String productTitle;
	private String lastMessage;
	private LocalDateTime lastMessageTime;
	private int unreadCount;
	private List<String> users;
	private Long productId;
	private LocalDateTime updatedAt;
}
