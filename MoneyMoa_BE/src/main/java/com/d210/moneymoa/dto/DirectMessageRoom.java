package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Document(collection = "directMessage_room")
@NoArgsConstructor
public class DirectMessageRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    private String id; // MongoDB는 주로 문자열 형식의 ID를 사용합니다. (예: ObjectId)
    private String roomId;
    private Long senderId;
    private String sender;
    private String receiver;
    private Long receiverId;

    @Builder
    public DirectMessageRoom(String sender, String receiver, Long senderId, Long receiverId) {
        this.roomId = UUID.randomUUID().toString();
        this.sender = sender;
        this.senderId = senderId;
        this.receiver = receiver;
        this.receiverId = receiverId;
    }
}
