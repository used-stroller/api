package team.three.usedstroller.api.service;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.domain.Message;
import team.three.usedstroller.api.domain.Message.MessageType;

@Service
@RequiredArgsConstructor
public class SocketService {
  private final MessageService messageService;

  public void sendSocketmessage(SocketIOClient senderClient, Message message,String room){
    for(SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()){
      if(!client.getSessionId().equals(senderClient.getSessionId())){ //보낸사람, 받는사람 다를경우
        client.sendEvent("read_message",message);
      }
    }
  }

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
