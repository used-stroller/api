package team.three.usedstroller.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.socket.ChatController.Message;
import team.three.usedstroller.api.socket.ChatController.MessageRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;

  public List<Message> getMessage(String room){
    return messageRepository.findAllByRoom(room);
  }

  public Message saveMessage(Message message){
    return messageRepository.save(message);
  }
}
