package team.three.usedstroller.api.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.three.usedstroller.api.chat.document.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
	// users 리스트에 특정 userId가 포함된 채팅방을 조회
	List<ChatRoom> findByUsersContainsOrderByUpdatedAtDesc(String userId);
}
