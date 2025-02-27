package team.three.usedstroller.api.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.three.usedstroller.api.chat.document.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
