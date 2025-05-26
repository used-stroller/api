package team.three.usedstroller.api.chat.document;

import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatRoom {
  @Id
  private String roomId; // 123_1_3
  private List<String> users; // 유저 ID 리스트,
  private String lastMessage; // 마지막 메시지
  private LocalDateTime lastMessageTime;
  private Long productId;
  private LocalDateTime updatedAt;

}
