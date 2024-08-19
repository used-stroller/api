package team.three.usedstroller.api.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.three.usedstroller.api.service.MessageService;

@Controller
public class ChatController {

  private final SocketIOServer server;

  public ChatController(SocketIOServer server) {
    this.server = server;
  }

  @OnConnect
  public void onConnect(SocketIOClient client) {
    System.out.println("Client connected: " + client.getSessionId());
  }

  @OnDisconnect
  public void onDisconnect(SocketIOClient client) {
    System.out.println("Client disconnected: " + client.getSessionId());
  }

  @OnEvent("message")
  public void onMessage(SocketIOClient client, String message) {
    System.out.println("Message received: " + message);
    server.getBroadcastOperations().sendEvent("message", message);
  }

  @Service
  @RequiredArgsConstructor
  public static class SocketIOService {
      private final SocketIOServer socketIOServer;

        @OnEvent("message")
        public void onMessage(SocketIOClient client, String data){
          System.out.println("data = " + data);
        }
  }

  @Repository
  public static interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByRoom(String room);
  }

  @RestController
  @RequestMapping("/message")
  @RequiredArgsConstructor
  public static class MessageController {
    private final MessageService messageService;

    @CrossOrigin(origins="http://localhost:5173")
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessage(@PathVariable String room){
      System.out.println("컨트롤러 호출됨");
      return ResponseEntity.ok(messageService.getMessage(room));
    }
  }

  @Builder
  @Entity
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column
    private String room;

    @Column
    private String username;

    @Column
    private String message;

    @Column
    private LocalDateTime createdAt;

    public enum MessageType {
      SERVER, CLIENT
    }
  }
}
