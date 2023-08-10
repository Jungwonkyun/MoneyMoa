package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;


@Getter
@Setter
public class ChatMessage implements Serializable {

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long memberId) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.memberId = memberId;
    }


    // 메시지 타입 : 입장, 퇴장, 채팅
    public enum MessageType {
        ENTER, QUIT, TALK, JOIN
    }

    @Enumerated(EnumType.STRING)
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    private long memberId; //해당 멤버 아이디


}