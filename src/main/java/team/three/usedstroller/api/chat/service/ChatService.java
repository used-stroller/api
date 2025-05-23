package team.three.usedstroller.api.chat.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.three.usedstroller.api.chat.document.ChatMessage;
import team.three.usedstroller.api.chat.document.ChatRoom;
import team.three.usedstroller.api.chat.dto.ChatMessageDto;
import team.three.usedstroller.api.chat.dto.CreateChatDto;
import team.three.usedstroller.api.chat.repository.ChatMessageRepository;
import team.three.usedstroller.api.chat.repository.ChatRoomRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatMessageDto> getChatHistory(String roomId) {

        List<ChatMessageDto> chatHistory = new ArrayList<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(roomId);
        for (ChatMessage chat : chatMessages) {
            chatHistory.add
                (ChatMessageDto.builder()
                .id(chat.getId())
                .sender(chat.getSender())
                .receiver(chat.getReceiver())
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
                .id(chatRoomId)
                .lastMessage("")
                .users(req.getUserIds())
                .lastMessageTimeStamp(System.currentTimeMillis())
                .build();
            return chatRoomRepository.save(chatRoom);
        });
    }
}
