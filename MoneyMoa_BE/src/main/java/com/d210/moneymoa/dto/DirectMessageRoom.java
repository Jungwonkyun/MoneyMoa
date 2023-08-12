package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class DirectMessageRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    private String id; // MongoDB는 주로 문자열 형식의 ID를 사용합니다. (예: ObjectId)
    private String roomId;
    private String sender;
    private String receiver;

    @Builder
    public DirectMessageRoom(String sender, String receiver) {
        this.roomId = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
    }
}
