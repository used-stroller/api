package team.three.usedstroller.api.chat.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.chat.document.ChatRoom;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomDto {
	private String id; // UUID 방 id
	private String roomId; // 123_1_3
	private List<String> users; // 유저 ID 리스트
	private String lastMessage; // 마지막 메시지
	private Long lastMessageTimeStamp;
	private Long productId;

	public static ChatRoomDto of(ChatRoom chatRoom) {
		return ChatRoomDto.builder()
			.id(chatRoom.getId())
			.roomId(chatRoom.getRoomId())
			.users(chatRoom.getUsers())
			.lastMessage(chatRoom.getLastMessage())
			.lastMessageTimeStamp(chatRoom.getLastMessageTimeStamp())
			.productId(chatRoom.getProductId())
			.build();
	}
}
