package team.three.usedstroller.api.chat;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import team.three.usedstroller.api.chat.repository.ChatMessageRepository;
import team.three.usedstroller.api.chat.repository.ChatRoomRepository;

@RequiredArgsConstructor
@org.springframework.context.annotation.Configuration
public class SocketConfig {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  @Bean
  public SocketIOServer socketIOServer() {
    Configuration config = new Configuration();
    config.setHostname("0.0.0.0"); // 모든 네트워크 인터페이스에서 접속 허용 (0.0.0.0은 모든 IP 바인딩)
    config.setPort(9092);
    config.setTransports(Transport.WEBSOCKET,Transport.POLLING);
    config.setPingInterval(25000); // 25초마다 핑 전송
    config.setPingTimeout(60000); // 60초 동안 응답 없으면 연결 해제
    config.setOrigin("*");
    SocketIOServer server = new SocketIOServer(config);

    // 클라이언트로부터 "sendMessage" 이벤트가 수신되었을 때의 핸들러 등록
    return server;
  }
}
