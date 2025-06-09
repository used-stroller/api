package team.three.usedstroller.api.chat.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.chat.document.ChatMessage;
import team.three.usedstroller.api.chat.document.ChatRoom;
import team.three.usedstroller.api.chat.dto.ChatHistoryResDto;
import team.three.usedstroller.api.chat.dto.ChatMessageDto;
import team.three.usedstroller.api.chat.dto.ChatRoomDto;
import team.three.usedstroller.api.chat.dto.CreateChatDto;
import team.three.usedstroller.api.chat.repository.ChatMessageRepository;
import team.three.usedstroller.api.chat.repository.ChatRoomRepository;
import team.three.usedstroller.api.common.utils.EntityUtils;
import team.three.usedstroller.api.common.utils.SecurityUtil;
import team.three.usedstroller.api.error.ApiErrorCode;
import team.three.usedstroller.api.error.ApiException;
import team.three.usedstroller.api.product.domain.Product;
import team.three.usedstroller.api.product.repository.ProductRepository;
import team.three.usedstroller.api.users.entity.Account;
import team.three.usedstroller.api.users.repository.AccountRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    public ChatHistoryResDto getChatHistory(String roomId) {
        // 채팅 대상자 아닌사람이 조회할경우 예외처리
        Long currentUserId = SecurityUtil.getAccountId();
        ChatRoom room = EntityUtils.findOrThrow(chatRoomRepository.findByRoomId(roomId),ApiErrorCode.CHAT_ROOM_NOT_FOUND);
        String receiverId = room.getUsers().stream()
            .filter(id -> !Objects.equals(id,currentUserId.toString()))
            .findFirst()
            .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        boolean isParticipant = room.getUsers().contains(currentUserId.toString());
        if (!isParticipant) {
            throw new ApiException(ApiErrorCode.UNAUTHORIZED_CHAT_USER);
        }

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

      return ChatHistoryResDto.builder()
          .currentUserId(currentUserId.toString())
          .receiverId(receiverId)
          .chatMessages(chatHistory)
          .build();
    }



    public static String convertDateFormat(String inputDate) {
        LocalDateTime localDateTime = LocalDateTime.parse(inputDate, DateTimeFormatter.ISO_DATE_TIME);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월d일 a h시m분");
        String formattedDate = localDateTime.format(formatter).replace("AM","오전").replace("PM", "오후");
        return formattedDate;
    }

    public ChatRoomDto createChatRoom(CreateChatDto req) {

        // 1. userId 정렬
        List<String> sortedUserIds = new ArrayList<>(req.getUserIds());
        Collections.sort(sortedUserIds);

        // 2. 중복 여부 체크(상품 ID + 유저)
        Optional<ChatRoom> existing = chatRoomRepository.findByProductIdAndUsers(req.getProductId(), sortedUserIds);
        if (existing.isPresent()) {
            return existing.get().toDto();
        }

        // 3. UUID 생성 + 저장
        String chatRoomId = UUID.randomUUID().toString();
            ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chatRoomId)
                .lastMessage("")
                .users(sortedUserIds)
                .productId(req.getProductId())
                .lastMessageTime(LocalDateTime.now())
                .build();
            chatRoomRepository.save(chatRoom);
        return chatRoom.toDto();
    }

	public List<ChatRoomDto> getChatRooms(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUsersContainsOrderByUpdatedAtDesc(userId);
        return chatRooms.stream()
            // ChatRoomDto 리스트를 반환
            .map(room -> {
                try {
                    log.info("🔥 처리 중인 방: {}", room.getRoomId());
                    log.info("🔥 users: {}", room.getUsers());

                    List<String> users = room.getUsers();
                    if (users == null || users.size() < 2) {
                        throw new IllegalStateException("유저가 2명 이상이 아닌 채팅방입니다. roomId=" + room.getRoomId());
                    }

                    String opponentId = users.stream()
                        .filter(id -> !id.equals(userId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("상대방 ID를 찾을 수 없습니다. roomId=" + room.getRoomId() + ", userId=" + userId));

                    log.info("opponentId: {}", opponentId);

                    Long opponentLongId = Long.parseLong(opponentId); // 여기도 try-catch 가능
                    Account opponent = EntityUtils.findOrThrow(accountRepository.findById(opponentLongId), ApiErrorCode.MEMBER_NOT_FOUND);
                    Product product = EntityUtils.findOrThrow(productRepository.findById(room.getProductId()), ApiErrorCode.PRODUCT_NOT_FOUND);
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
                } catch (Exception e) {
                    log.error("❌ 채팅방 처리 실패: roomId={}, 오류: {}", room.getRoomId(), e.getMessage(), e);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();

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
