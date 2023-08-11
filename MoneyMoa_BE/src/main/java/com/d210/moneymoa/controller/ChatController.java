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
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            log.info("얘는 처음 들어오는 사람");
        }

        if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
//            chatRoomRepository.decreaseUserCount(message.getRoomId());  // User count 감소
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
        }

        log.info("message: "+ message);
        
        //채팅 내역 저장
        chatRoomService.saveChatMessage(message.getRoomId(), message);

        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }


//    @PostMapping("/chat/message")
//    public void message(@RequestBody ChatMessage message, @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) {
//
//        jwt = jwt.replaceAll("Bearer ", "");
//
//        Long memberId = authTokensGenerator.extractMemberId(jwt);
//        Optional<Member> oMember = memberRepository.findById(memberId);
//
//        try {
//            String nickName = oMember.get().getNickname();
//            message.setSender(nickName);
//            message.setMemberId(memberId);
//
//            if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//                chatRoomService.enterChatRoom(message.getRoomId());
//                //chatRoomService.increaseUserCount(message.getRoomId());
//                message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//            } else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
//                //chatRoomService.decreaseUserCount(message.getRoomId());  // User count 감소
//                message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
//            }
//            //채팅 내역 저장
//            chatRoomService.saveChatMessage(message.getRoomId(), message);
//
//            // Websocket에 발행된 메시지를 redis로 발행한다(publish)
//            redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//    }

}
