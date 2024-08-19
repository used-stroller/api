package team.three.usedstroller.api.socket.v2;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SockerIOConfig {

  @Value("${socket-server.host}")
  private String host;

  @Value("${socket-server.port}")
  private Integer port;

  /**
   *
   * Tomcat 서버와 별도로 돌아가는 netty 서버를 생성
   */
  @Bean
  public SocketIOServer socketIOServer(){
    com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
    config.setHostname(host);
    config.setPort(port);
    return new SocketIOServer(config);
  }
}
