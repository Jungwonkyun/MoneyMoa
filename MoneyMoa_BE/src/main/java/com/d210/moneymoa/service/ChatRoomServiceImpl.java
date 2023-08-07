package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.redis.RedisSubscriber;
import com.d210.moneymoa.dto.ChatMessage;
import com.d210.moneymoa.dto.ChatMessageDto;
import com.d210.moneymoa.dto.ChatRoom;
import com.d210.moneymoa.dto.ChatRoomDto;
import com.d210.moneymoa.repository.ChatMessageDtoRepository;
import com.d210.moneymoa.repository.ChatRoomDtoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @Autowired
    ChatMessageDtoRepository chatMessageDtoRepository;

    @Autowired
    ChatRoomDtoRepository chatRoomDtoRepository;

    public ChatRoomServiceImpl(RedisMessageListenerContainer redisMessageListener, RedisSubscriber redisSubscriber, RedisTemplate<String, Object> redisTemplate) {
        this.redisMessageListener = redisMessageListener;
        this.redisSubscriber = redisSubscriber;
        this.redisTemplate = redisTemplate;
    }


    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    @Transactional
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);

        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .build();

        chatRoomDtoRepository.save(chatRoomDto);

        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);

        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);

//        Optional<ChatRoomDto> oChatRoomDto = chatRoomDtoRepository.findByRoomId(roomId);
//        ChatRoomDto chatRoomDto =  oChatRoomDto.orElseGet(null);

        //chatRoomDto.setUserCount(chatRoomDto.getUserCount()+1);
//
//        chatRoomDtoRepository.save(chatRoomDto);
        //Optional<ChatRoomDto> oChatRoomDto = ChatRoomDtoRepository.findByRoomID(roomId);
    }

    public void saveChatMessage(String roomId, ChatMessage chatMessage) {
        log.info(chatMessage.toString());
        // 방 id로 채팅방 찾기
        ChatRoom chatRoom = findRoomById(roomId);

        ChatMessageDto chatMessages = ChatMessageDto.builder()
                .message(chatMessage.getMessage())
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .type(chatMessage.getType())
                .build();

        chatMessageDtoRepository.save(chatMessages);

        if (chatRoom != null) {

            // 채팅 메시지 리스트에 메시지 추가
            chatRoom.getChatmessage().add(chatMessage);
            // 변경된 채팅방 정보를 다시 Redis에 저장
            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        }
    }


//    public void increaseUserCount(String roomId) {
//        // 방 id로 채팅방 찾기
//        ChatRoom chatRoom = findRoomById(roomId);
//        if (chatRoom != null) {
//            // 유저 수 증가
//            chatRoom.setUserCount(chatRoom.getUserCount() + 1);
//            // 변경된 채팅방 정보를 다시 Redis에 저장
//            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//        }
//    }

//    public void decreaseUserCount(String roomId) {
//        // 방 id로 채팅방 찾기
//        ChatRoom chatRoom = findRoomById(roomId);
//        if (chatRoom != null && chatRoom.getUserCount() > 0) {
//            // 유저 수 감소
//            chatRoom.setUserCount(chatRoom.getUserCount() - 1);
//            // 변경된 채팅방 정보를 다시 Redis에 저장
//            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//        }
//    }
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
