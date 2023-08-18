package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@Document(collection = "chat_messages")
@NoArgsConstructor
public class ChatMessageDto implements Serializable {

    @Id
    private String id; //

    private ChatMessage.MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private long senderId; // 메시지 보낸사람
    private String message; // 메시지
    private LocalDateTime createdTime; // 메시지 생성 시간

    @Builder
    public ChatMessageDto(ChatMessage.MessageType type, String roomId, String sender, long senderId, String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.senderId = senderId;
        this.message = message;
        this.createdTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")); // 현재 시간 설정
    }

    @Override
    public String toString() {
        return "ChatmessageDto{" +
                "type=" + type +
                ", roomId='" + roomId + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}







//@Getter
//@Setter
//@Entity
//@NoArgsConstructor
//public class ChatMessageDto implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    @Enumerated(EnumType.STRING)
//    private ChatMessage.MessageType type; // 메시지 타입
//    private String roomId; // 방번호
//    private String sender; // 메시지 보낸사람
//    private String message; // 메시지
//
//
//    @Builder
//    public ChatMessageDto(ChatMessage.MessageType type, String roomId, String sender, String message) {
//        this.type = type;
//        this.roomId = roomId;
//        this.sender = sender;
//        this.message = message;
//    }
//
//    @Override
//    public String toString() {
//        return "ChatmessageDto{" +
//                "type=" + type +
//                ", roomId='" + roomId + '\'' +
//                ", sender='" + sender + '\'' +
//                ", message='" + message + '\'' +
//                '}';
//    }
//}
