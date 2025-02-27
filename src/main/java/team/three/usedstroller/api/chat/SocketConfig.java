package team.three.usedstroller.api.chat;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.listener.DataListener;
import org.json.JSONException;
import org.springframework.context.annotation.Bean;
import team.three.usedstroller.api.chat.document.ChatMessage;

@org.springframework.context.annotation.Configuration
public class SocketConfig {
  @Bean
  public SocketIOServer socketIOServer() {
    Configuration config = new Configuration();
    config.setHostname("0.0.0.0");
    config.setPort(9092);
    config.setTransports(Transport.WEBSOCKET,Transport.POLLING); // WebSocket과 Polling 모두 허용
    config.setPingInterval(25000); // 25초마다 핑 전송
    config.setPingTimeout(60000); // 60초 동안 응답 없으면 연결 해제
    config.setOrigin("*");
    SocketIOServer server = new SocketIOServer(config);

    server.addEventListener("sendMessage", ChatMessage.class, new DataListener<ChatMessage>() {
      @Override
      public void onData(SocketIOClient client, ChatMessage data, AckRequest ackRequest)
          throws JSONException {
        String roomId = data.getRoomId();
        String sender = data.getSender();
        String message = data.getMessage();
        server.getRoomOperations(roomId).sendEvent("message", message);
      }
    });
    return server;
  }
}
