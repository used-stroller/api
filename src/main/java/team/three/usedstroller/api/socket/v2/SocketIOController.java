package team.three.usedstroller.api.socket.v2;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.socket.ChatController.Message;
import team.three.usedstroller.api.socket.ChatController.Message.MessageType;
import team.three.usedstroller.api.service.MessageService;

@Component
@Slf4j
public class SocketIOController {
    private final SocketIOServer server;

  /**
   * 소켓 이벤트 리스너 등록
   */
  public SocketIOController(SocketIOServer server){
    this.server=server;
    server.addConnectListener(listenConnected());
    server.addDisconnectListener(listenDisconnected());
  }

  /**
   * 클라이언트 연결 리스너
   */
  public ConnectListener listenConnected() {
    return (client) -> {
      Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
      log.info("connect:" + params.toString());
    };
  }

  /**
   * 클라이언트 연결 해제 리스너
   */
  public DisconnectListener listenDisconnected() {
    return client -> {
      String sessionId = client.getSessionId().toString();
      log.info("disconnect: " + sessionId);
      client.disconnect();
    };
  }

  @Service
  @RequiredArgsConstructor
  public static class SocketService {
    private final MessageService messageService;

    /**
     * 발신자를 제외한 룸의 모든 클라이언트에게 메시지를 보냄
     * @param senderClient 메시지를 보내는 클라이언트
     * @param message 메시지
     * @param room 메시지를 보낼 방
     */
    public void sendSocketmessage(SocketIOClient senderClient, Message message,String room){
      for(SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()){
        if(!client.getSessionId().equals(senderClient.getSessionId())){ //보낸사람, 받는사람 다를경우
          client.sendEvent("read_message",message);
        }
      }
    }

    /**
     * 클라이언트 메시지를 저장한 다음 다른 클라이언트에게 보냄
     * @param senderClient 메시지를 보내는 클라이언트
     * @param message 메시지
     */
    public void saveMessage(SocketIOClient senderClient, Message message){
      Message storedMessage = messageService.saveMessage(
          Message.builder()
              .messageType(MessageType.CLIENT)
              .message(message.getMessage())
              .room(message.getRoom())
              .username(message.getUsername())
              .build()
      );

      sendSocketmessage(senderClient,storedMessage, message.getRoom());
    }

    /**
     * 서버메세지를 저장한 다음 다른 클라이언트에게 보냄
     * @param senderClient
     * @param message
     * @param room
     */
    public void saveInfoMessage(SocketIOClient senderClient, String message, String room){
      Message storedMessage = messageService.saveMessage(
          Message.builder()
              .messageType(MessageType.SERVER)
              .message(message)
              .room(room)
              .build()
      );

      sendSocketmessage(senderClient,storedMessage, room);
    }
  }
}
