package com.d210.moneymoa.controller;


import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.domain.redis.RedisPublisher;
import com.d210.moneymoa.dto.ChatMessage;
import com.d210.moneymoa.dto.MemberChatroomSubInfo;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.web.bind.annotation.Controller;
import org.springframework.stereotype.Controller;
// TODO: 2023-08-04 REST Controller로 바꿔야함
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    AuthTokensGenerator authTokensGenerator;


    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */

    @MessageMapping("/api/chat/message")
    public void message(ChatMessage message) {

        if(chatRoomService.enterChatRoom(message.getMemberId(), message.getRoomId()) != null){
            message.setType(ChatMessage.MessageType.JOIN);
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("얘는 처음 들어오는 사람");
        }

        if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }
        log.info("message: "+ message);
        //채팅 내역 저장
        chatRoomService.saveChatMessage(message.getRoomId(), message);
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }


    @MessageMapping("/api/chat/dm")
    public void directMessage(ChatMessage message) {

        if(chatRoomService.enterDMRoom(message.getMemberId(), message.getRoomId()) != null){
            message.setType(ChatMessage.MessageType.JOIN);
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("얘는 처음 들어오는 사람");
        }

        if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }

        //채팅 내역 저장
        chatRoomService.saveChatMessage(message.getRoomId(), message);
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }

}
