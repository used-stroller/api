package team.three.usedstroller.api.socket.v2;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class SocketIoServerLifeCycle {
  private final SocketIOServer server;

  public SocketIoServerLifeCycle(SocketIOServer server) {
    this.server = server;
  }

  /**
   * SocketIo 서버 시작
   */
  @PostConstruct
  public void start() {
    server.start();
  }

  /**
   * SocketIo 서버 종료
   */
  @PreDestroy
  public void stop() {
    server.stop();
  }
}
