package team.three.usedstroller.api.chat.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.three.usedstroller.api.chat.document.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
  List<ChatMessage> findByRoomId(String roomId); // 특정 채팅방의 메시지 가져오기

	int countByRoomIdAndReceiverIdAndReadFalse(String roomId, String userId);
}
