package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.*;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

public interface ChatRoomService {

//    public List<ChatRoom> findAllRoom();

    //public ChatRoom findRoomById(String id);

    public ChatRoomDto findRoomByRoomId(String id);

    // public ChatRoom createChatRoom(String name);

    public ChatRoomDto createChatRoom(ChatRoomDto chatRoom);

    //public void enterChatRoom(String roomId);

    public MemberChatroomSubInfo enterChatRoom(long memberId, String roomId);

    //public void saveChatMessage(String roomId, ChatMessage chatMessage);
    // List<ChatRoomDto> saveChatMessage(String roomId, ChatMessage chatMessage);

    public List<ChatMessageDto> saveChatMessage(String roomId, ChatMessage chatMessage);

//    public void increaseUserCount(String roomId);

//    public void decreaseUserCount(String roomId);
    public ChannelTopic getTopic(String roomId);

    public List<ChatRoomDto> findAllRoomFromDB();

    ChatRoomDto findRoomByName(String name);

    List<MemberChatroomSubInfo> chatRoomMembers(String roomId);

    void getOutchatRoom(Long memberId, String roomId);
}
