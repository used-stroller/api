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
  private String sender;
  private String receiver;
  private String message;
  private String timestamp;
}
