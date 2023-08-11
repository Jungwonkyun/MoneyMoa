package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.redis.RedisSubscriber;
import com.d210.moneymoa.dto.*;
import com.d210.moneymoa.repository.ChatMessageDtoRepository;
import com.d210.moneymoa.repository.ChatRoomDtoRepository;
import com.d210.moneymoa.repository.MemberChatroomSubInfoRepository;
import com.d210.moneymoa.repository.MemberRepository;
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
import java.util.Optional;

@Slf4j
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;

    //private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    private HashOperations<String, String, ChatRoomDto> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;

    @Autowired
    ChatMessageDtoRepository chatMessageDtoRepository;

    @Autowired
    ChatRoomDtoRepository chatRoomDtoRepository;

    @Autowired
    MemberChatroomSubInfoRepository memberChatroomSubInfoRepository;

    @Autowired
    MemberRepository memberRepository;

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

//    public List<ChatRoom> findAllRoom() {
//        return opsHashChatRoom.values(CHAT_ROOMS);
//    }

    public List<ChatRoomDto> findAllRoomFromDB() {
        return chatRoomDtoRepository.findAll();
    }

    public ChatRoomDto findRoomByName(String name) {
        Optional<ChatRoomDto> oChatRoomDto =  chatRoomDtoRepository.findByName(name);
        ChatRoomDto chatRoomDto = oChatRoomDto.orElse(null);
        return chatRoomDto;
    }

    @Override
    public List<MemberChatroomSubInfo> chatRoomMembers(String roomId) {
        List<MemberChatroomSubInfo> memberSubList = memberChatroomSubInfoRepository.findByRoomId(roomId);
        return memberSubList;
    }

    @Override
    public void getOutchatRoom(Long memberId, String roomId) {
        Optional<MemberChatroomSubInfo> memberChatroomSubInfo = memberChatroomSubInfoRepository.findByMemberIdAndRoomId(memberId, roomId);
        memberChatroomSubInfo.ifPresent(memberChatroomSubInfoRepository::delete);
        return;
    }

//    public ChatRoom findRoomById(String id) {
//        return opsHashChatRoom.get(CHAT_ROOMS, id);
//    }

    /**
     * 채팅방 검색 : 채팅방 id를 통해서 정보 받기
     */
    public ChatRoomDto findRoomByRoomId(String roomId) {
        Optional<ChatRoomDto>oChatRoomDto = chatRoomDtoRepository.findByRoomId(roomId);
        //return opsHashChatRoom.get(CHAT_ROOMS, id);
        return oChatRoomDto.orElse(null);
    }


    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
//    @Transactional
//    public ChatRoom createChatRoom(String name) {
//        ChatRoom chatRoom = ChatRoom.create(name);
//
//        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
//                .roomId(chatRoom.getRoomId())
//                .name(chatRoom.getName())
//                .build();
//
//        chatRoomDtoRepository.save(chatRoomDto);
//        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
//
//        return chatRoom;
//    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    @Transactional
    public ChatRoomDto createChatRoom(ChatRoomDto inputChatRoomDto) {
        ChatRoom chatRoom = ChatRoom.create(inputChatRoomDto.getName());

        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .roomId(inputChatRoomDto.getRoomId())
                .name(inputChatRoomDto.getName())
                .description(inputChatRoomDto.getDescription())
                .imgUrl(inputChatRoomDto.getImgUrl())
                .build();

        chatRoomDtoRepository.save(chatRoomDto);

        //opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoomDto);
        return chatRoomDto;
    }


    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public MemberChatroomSubInfo enterChatRoom(long memberId, String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);


        //이미 구독하고 있는지 체크
        Optional<MemberChatroomSubInfo> optionalMember = memberChatroomSubInfoRepository.findByMemberIdAndRoomId(memberId,roomId);

        if (optionalMember.isPresent()) {
            log.info("사용자 {}는 이미 roomId {}에 구독 중입니다.", memberId, roomId);
            return null;
        }
        
        log.info("사용자 {}는 roomId {}에 처음 입장합니다.", memberId, roomId);
        
        String nickName = memberRepository.findById(memberId).get().getNickname();

        // MemberChatroomSubInfo findMember = memberChatroomSubInfoRepository.findByMemberIdAndRoomId(memberId,roomId).orElse(null);
        // if(findMember!=null)return null;
        
        //구독 정보 DB에 저장
        MemberChatroomSubInfo memberChatroomSubInfo = MemberChatroomSubInfo.builder()
                .memberId(memberId)
                .memberNickname(nickName)
                .roomId(roomId)
                .build();

        memberChatroomSubInfoRepository.save(memberChatroomSubInfo);

        return memberChatroomSubInfo;
    }

//    public void saveChatMessage(String roomId, ChatMessage chatMessage) {
//        log.info(chatMessage.toString());
//        // 방 id로 채팅방 찾기
//        //ChatRoom chatRoom = findRoomById(roomId);
//
//        ChatMessageDto chatMessages = ChatMessageDto.builder()
//                .message(chatMessage.getMessage())
//                .roomId(chatMessage.getRoomId())
//                .sender(chatMessage.getSender())
//                .type(chatMessage.getType())
//                .build();
//
//        chatMessageDtoRepository.save(chatMessages);
//
////        if (chatRoom != null) {
////
////            // 채팅 메시지 리스트에 메시지 추가
////            chatRoom.getChatmessage().add(chatMessage);
////            // 변경된 채팅방 정보를 다시 Redis에 저장
////            opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
////        }
//    }

//    public List<ChatRoomDto> saveChatMessage(String roomId, ChatMessage chatMessage) {
//        log.info(chatMessage.toString());
//
//        ChatMessageDto chatMessages = ChatMessageDto.builder()
//                .message(chatMessage.getMessage())
//                .roomId(chatMessage.getRoomId())
//                .sender(chatMessage.getSender())
//                .type(chatMessage.getType())
//                .build();
//
//        chatMessageDtoRepository.save(chatMessages);
//
//        return chatRoomDtoRepository.findAll();
//    }

    public List<ChatMessageDto> saveChatMessage(String roomId, ChatMessage chatMessage) {
        log.info(chatMessage.toString());

        ChatMessageDto chatMessages = ChatMessageDto.builder()
                .message(chatMessage.getMessage())
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .senderId(chatMessage.getMemberId())
                .type(chatMessage.getType())
                .build();

        chatMessageDtoRepository.save(chatMessages);

        //return chatRoomDtoRepository.findAll();
        return chatMessageDtoRepository.findByRoomId(roomId);
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

    public List<ChatMessageDto> getChatMessages(String roomId){
        return chatMessageDtoRepository.findByRoomId(roomId);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
