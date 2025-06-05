package team.three.usedstroller.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
  private String id;
  private String roomId;
  private String senderId;
  private String receiverId;
  private String message;
  private String timestamp;
}
