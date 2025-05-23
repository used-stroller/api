package team.three.usedstroller.api.chat.service;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import team.three.usedstroller.api.chat.document.ChatMessage;
import team.three.usedstroller.api.chat.repository.ChatMessageRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSocketHandler {

  private final SocketIOServer server;
  private final ChatMessageRepository repository;
  private boolean isStarted = false;

  @PostConstruct
  private void init() {
    if (!isStarted) {
      isStarted = true;

    server.addConnectListener(client -> System.out.println("클라이언트 연결됨: " + client.getSessionId()));
    server.addEventListener("joinRoom", String.class, (client, roomId, askSender) -> {
      client.joinRoom(roomId);
      System.out.println("방 입장 : " + roomId);
    });
    server.addEventListener("sendMessage", ChatMessage.class, (client, message, ackSender) -> {
      System.out.println("📩 메시지 수신: " + message.getMessage()); // 로그 추가
      message.setTimestamp(LocalDateTime.now().toString());
      repository.save(message);
      message.setTimestamp(ChatService.convertDateFormat(message.getTimestamp()));
      server.getRoomOperations(message.getRoomId()).sendEvent("receiveMessage", message);
    });

      server.addDisconnectListener(client -> {
        System.out.println("클라이언트 연결 종료됨: " + client.getSessionId());
        // 연결 종료 후 필요한 작업 처리
      });
    server.start();
    }
      else {
        System.out.println("⚠️ 이미 Socket.IO 서버가 실행 중입니다.");
      }
  }

  @PreDestroy
  public void shutdown() {
    if (isStarted) {
      log.info("Socket.IO 서버 종료중...");
      server.stop();
    }
  }

}
