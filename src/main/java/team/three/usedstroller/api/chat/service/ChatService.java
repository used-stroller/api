package team.three.usedstroller.api.chat.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.text.html.parser.Entity;

import lombok.RequiredArgsConstructor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.chat.document.ChatMessage;
import team.three.usedstroller.api.chat.document.ChatRoom;
import team.three.usedstroller.api.chat.dto.ChatMessageDto;
import team.three.usedstroller.api.chat.dto.ChatRoomDto;
import team.three.usedstroller.api.chat.dto.CreateChatDto;
import team.three.usedstroller.api.chat.repository.ChatMessageRepository;
import team.three.usedstroller.api.chat.repository.ChatRoomRepository;
import team.three.usedstroller.api.common.utils.EntityUtils;
import team.three.usedstroller.api.common.utils.SecurityUtil;
import team.three.usedstroller.api.error.ApiErrorCode;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.repository.ProductRepository;
import team.three.usedstroller.api.users.entity.Account;
import team.three.usedstroller.api.users.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    public List<ChatMessageDto> getChatHistory(String roomId) {

        // 모든 메시지 읽음 표시
        markMessagesAsRead(roomId,SecurityUtil.getAccountId().toString());

        List<ChatMessageDto> chatHistory = new ArrayList<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(roomId);
        for (ChatMessage chat : chatMessages) {
            chatHistory.add
                (ChatMessageDto.builder()
                .id(chat.getId())
                .senderId(chat.getSenderId())
                .receiverId(chat.getReceiverId())
                .message(chat.getMessage())
                .roomId(chat.getRoomId())
                .timestamp(convertDateFormat(chat.getTimestamp()))
                .build());
        }
      return chatHistory;
    }



    public static String convertDateFormat(String inputDate) {
        LocalDateTime localDateTime = LocalDateTime.parse(inputDate, DateTimeFormatter.ISO_DATE_TIME);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월d일 a h시m분");
        String formattedDate = localDateTime.format(formatter).replace("AM","오전").replace("PM", "오후");
        return formattedDate;
    }

    public ChatRoom createChatRoom(CreateChatDto req) {
        Collections.sort(req.getUserIds());
        String chatRoomId = req.getProductId()+"_"+String.join("_", req.getUserIds());
        return chatRoomRepository.findById(chatRoomId).orElseGet(() -> {
            ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chatRoomId)
                .lastMessage("")
                .users(req.getUserIds())
                .lastMessageTime(LocalDateTime.now())
                .build();
            return chatRoomRepository.save(chatRoom);
        });
    }

	public List<ChatRoomDto> getChatRooms(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUsersContainsOrderByUpdatedAtDesc(userId);
        return chatRooms.stream()
            // ChatRoomDto 리스트를 반환
            .map(room -> {
                String opponentId = room.getUsers().stream()
                    .filter(id -> !id.equals(userId)) //사용자아이디 아닌것 찾기
                    .findFirst().orElse(null);
                Account opponent = EntityUtils.findOrThrow(accountRepository.findById(Long.parseLong(opponentId)),
                    ApiErrorCode.MEMBER_NOT_FOUND);
                Product product = EntityUtils.findOrThrow(productRepository.findById(room.getProductId()),ApiErrorCode.PRODUCT_NOT_FOUND);
                int unreadCount = chatMessageRepository.countByRoomIdAndReceiverIdAndReadFalse(room.getRoomId(), userId);
                return ChatRoomDto.builder()
                    .roomId(room.getRoomId())
                    .opponentName(opponent.getName())
                    .productImageUrl(product.getImgSrc())
                    .productTitle(product.getTitle())
                    .lastMessage(room.getLastMessage())
                    .lastMessageTime(room.getLastMessageTime())
                    .unreadCount(unreadCount)
                    .build();
            }).toList();
	}


    // 읽음 처리
    public void markMessagesAsRead(String roomId, String receiverId) {
        Query query = new Query(
            Criteria.where("roomId").is(roomId)
                .and("receiverId").is(receiverId)
                .and("read").is(false)
        );
        Update update = new Update().set("read",true);
        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }
}
