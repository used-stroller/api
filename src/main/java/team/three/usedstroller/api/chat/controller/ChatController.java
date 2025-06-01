package team.three.usedstroller.api.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.chat.document.ChatRoom;
import team.three.usedstroller.api.chat.dto.ChatMessageDto;
import team.three.usedstroller.api.chat.dto.ChatRoomDto;
import team.three.usedstroller.api.chat.dto.CreateChatDto;
import team.three.usedstroller.api.chat.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  // 특정 채팅방의 메시지 가져오기
  @GetMapping("/history/{roomId}")
  public List<ChatMessageDto> getChatHistory(@PathVariable String roomId) {
    return chatService.getChatHistory(roomId);
  }

  @PostMapping("/create")
  public ChatRoom createChatRoom(@RequestBody CreateChatDto req) {
    return chatService.createChatRoom(req);
  }

  @GetMapping("/list")
  public List<ChatRoomDto> getChatRooms(String userId) {
    return chatService.getChatRooms(userId);
  }
}
