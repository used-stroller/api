package team.three.usedstroller.api.chat.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "messages")
public class ChatMessage {
  @Id
  private String id;
  private String roomId;
  private String senderId;
  private String receiverId;
  private String message;
  private String timestamp;
  private boolean read = false;
}
