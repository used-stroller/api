package team.three.usedstroller.api.chat.document;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import team.three.usedstroller.api.chat.dto.ChatRoomDto;

@Document(collection = "chat_rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoom {
  private String roomId; // 123_1_3
  private List<String> users; // 유저 ID 리스트,
  private String lastMessage; // 마지막 메시지
  private LocalDateTime lastMessageTime;
  private Long productId;
  private LocalDateTime updatedAt;

  public ChatRoomDto toDto() {
    return ChatRoomDto.builder()
        .roomId(this.roomId)
        .users(this.users)
        .lastMessage(this.lastMessage)
        .lastMessageTime(this.lastMessageTime)
        .productId(this.productId)
        .updatedAt(this.updatedAt)
        .build();
  }
}
