package team.three.usedstroller.api.chat.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import team.three.usedstroller.api.chat.document.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
	// users 리스트에 특정 userId가 포함된 채팅방을 조회
	List<ChatRoom> findByUsersContainsOrderByUpdatedAtDesc(String userId);
	Optional<ChatRoom> findByRoomId(String roomId);
	// 사용자 ID와 productId로 채팅방 검색
	Optional<ChatRoom> findByProductIdAndUsers(Long productId, List<String> sortedUserIds);
}
