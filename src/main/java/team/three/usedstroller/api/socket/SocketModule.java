//package team.three.usedstroller.api.socket;
//
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.listener.ConnectListener;
//import com.corundumstudio.socketio.listener.DataListener;
//import com.corundumstudio.socketio.listener.DisconnectListener;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import team.three.usedstroller.api.socket.ChatController.Message;
//import team.three.usedstroller.api.socket.v2.SocketIOController.SocketService;
//import team.three.usedstroller.api.utils.Constants;
//
//@Slf4j
//@Component
//public class SocketModule { // SocketIOServer를 사용하여 소켓 연결에 대한 설정 및 이벤트 처리
//  private final SocketIOServer server;
//  private final SocketService socketService;
//
//  // 이벤트 리스너 등록
//  public SocketModule(SocketIOServer server, SocketService socketService){
//    this.server = server;
//    this.socketService = socketService;
//    server.addConnectListener(this.onConnected());
//    server.addDisconnectListener(this.onDisconnected());
//    server.addEventListener("send_message", Message.class,this.onChatReceived());
//  }
//
//  private DataListener<Message> onChatReceived(){
//    return (senderClient, data, ackSender) -> {
//      log.info(data.toString());
//      socketService.saveMessage(senderClient,data);
//    };
//  }
//
//  private ConnectListener onConnected(){ //매개변수를 검색하고, 클라이언트를 지정된 방에 참여시킴
//    return (client) -> {
//      var params = client.getHandshakeData().getUrlParams();
//      String room = params.get("room").stream().collect(Collectors.joining());
//      String username = params.get("username").stream().collect(Collectors.joining());
//      client.joinRoom(room);
//      socketService.saveInfoMessage(client,String.format(Constants.WELCOME_MESSAGE,username),room);
//      log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
//    };
//  }
//
//  private DisconnectListener onDisconnected(){
//    return client -> {
//      var params = client.getHandshakeData().getUrlParams();
//      String room = params.get("room").stream().collect(Collectors.joining());
//      String username = params.get("username").stream().collect(Collectors.joining());
//      socketService.saveInfoMessage(client,String.format(Constants.DISCONNECT_MESSAGE,username),room);
//      log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
//    };
//  }
//
//}
