package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.redis.RedisSubscriber;
import com.d210.moneymoa.dto.*;
import com.d210.moneymoa.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

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

    @Autowired
    DirectMessageRoomRepository directMessageRoomRepository;

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



    public List<ChatRoomDto> findAllRoomFromDB() {
        return chatRoomDtoRepository.findAll();
    }

    public List<DirectMessageRoom> findAllDMFromDB(Long memberId) {
        return directMessageRoomRepository.findAllDirectMessageRoomBySenderIdOrReceiverId(memberId, memberId);
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


    private ChannelTopic getOrCreateTopic(String fullRoomId) {
        return topics.computeIfAbsent(fullRoomId, ChannelTopic::new);
    }

    private MessageListenerAdapter createAdapter(String methodName) {
        return new MessageListenerAdapter(redisSubscriber, methodName);
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public MemberChatroomSubInfo enterChatRoom(long memberId, String roomId) {
//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null)
//            topic = new ChannelTopic(roomId);
//        redisMessageListener.addMessageListener(redisSubscriber, topic);
//        topics.put(roomId, topic);

        ChannelTopic roomTopic = getOrCreateTopic("room:" + roomId);
        redisMessageListener.addMessageListener(createAdapter("onMessage"), roomTopic);

        //이미 구독하고 있는지 체크
        Optional<MemberChatroomSubInfo> optionalMember = memberChatroomSubInfoRepository.findByMemberIdAndRoomId(memberId,roomId);

        if (optionalMember.isPresent()) {
            log.info("사용자 {}는 이미 roomId {}에 구독 중입니다.", memberId, roomId);
            return null;
        }
        
        log.info("사용자 {}는 roomId {}에 처음 입장합니다.", memberId, roomId);
        
        String nickName = memberRepository.findById(memberId).get().getNickname();

        
        //구독 정보 DB에 저장
        MemberChatroomSubInfo memberChatroomSubInfo = MemberChatroomSubInfo.builder()
                .memberId(memberId)
                .memberNickname(nickName)
                .roomId(roomId)
                .build();

        memberChatroomSubInfoRepository.save(memberChatroomSubInfo);

        return memberChatroomSubInfo;
    }

    public List<ChatMessageDto> saveChatMessage(String roomId, ChatMessage chatMessage) {
        log.info(chatMessage.toString());

        //빈 문자열 못 보내게
        if(chatMessage.getMessage()!=null) {
            ChatMessageDto chatMessages = ChatMessageDto.builder()
                    .message(chatMessage.getMessage())
                    .roomId(chatMessage.getRoomId())
                    .sender(chatMessage.getSender())
                    .senderId(chatMessage.getMemberId())
                    .type(chatMessage.getType())
                    .build();

            chatMessageDtoRepository.save(chatMessages);
        }
        return chatMessageDtoRepository.findByRoomId(roomId);
    }

    public List<MemberChatroomSubInfo> sendDirectMessage(Long senderId, Long sendedId){

        String nickName1 = memberRepository.findById(senderId).get().getNickname();
        String nickName2 = memberRepository.findById(sendedId).get().getNickname();

        DirectMessageRoom DmRoom = DirectMessageRoom.builder()
                .sender(nickName1)
                .senderId(senderId)
                .receiver(nickName2)
                .receiverId(sendedId)
                .build();

        directMessageRoomRepository.save(DmRoom);

        MemberChatroomSubInfo memberChatroomSubInfo1 = MemberChatroomSubInfo.builder()
                .memberId(senderId)
                .memberNickname(nickName1)
                .roomId(DmRoom.getRoomId())
                .build();

        MemberChatroomSubInfo memberChatroomSubInfo2 = MemberChatroomSubInfo.builder()
                .memberId(sendedId)
                .memberNickname(nickName2)
                .roomId(DmRoom.getRoomId())
                .build();

        memberChatroomSubInfoRepository.save(memberChatroomSubInfo1);
        memberChatroomSubInfoRepository.save(memberChatroomSubInfo2);

        List<MemberChatroomSubInfo>subList = new ArrayList<>();
        subList.add(memberChatroomSubInfo1);
        subList.add(memberChatroomSubInfo2);

        return subList;
    }

    @Override
    public DirectMessageRoom findDMRoomByRoomId(String roomId) {
        Optional<DirectMessageRoom>optionalDirectMessageRoomDto = directMessageRoomRepository.findByRoomId(roomId);
        return optionalDirectMessageRoomDto.orElse(null);
    }

    public MemberChatroomSubInfo enterDMRoom(long memberId, String roomId) {

//        ChannelTopic topic = topics.get(roomId);
//        if (topic == null)
//            topic = new ChannelTopic(roomId);
//        redisMessageListener.addMessageListener(redisSubscriber, topic);
//        topics.put(roomId, topic);

        // DM 메시지용 토픽
        ChannelTopic dmTopic = getOrCreateTopic("dm:" + roomId);
        redisMessageListener.addMessageListener(createAdapter("onDirectMessage"), dmTopic);

        //이미 구독하고 있는지 체크
        Optional<MemberChatroomSubInfo> optionalMember = memberChatroomSubInfoRepository.findByMemberIdAndRoomId(memberId,roomId);
        return optionalMember.orElse(null);

    }



    public List<ChatMessageDto> getChatMessages(String roomId){
        return chatMessageDtoRepository.findByRoomId(roomId);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }
}
