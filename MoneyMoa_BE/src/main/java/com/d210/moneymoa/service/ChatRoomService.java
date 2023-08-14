package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.*;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.List;

public interface ChatRoomService {

    ChatRoomDto findRoomByRoomId(String id);


    ChatRoomDto createChatRoom(ChatRoomDto chatRoom);


    MemberChatroomSubInfo enterChatRoom(long memberId, String roomId);
    MemberChatroomSubInfo enterDMRoom(long memberId, String roomId);

    List<ChatMessageDto> saveChatMessage(String roomId, ChatMessage chatMessage);

     ChannelTopic getTopic(String roomId);

    List<ChatRoomDto> findAllRoomFromDB();
    List<DirectMessageRoom> findAllDMFromDB(Long memberId);

    ChatRoomDto findRoomByName(String name);

    List<MemberChatroomSubInfo> chatRoomMembers(String roomId);

    void getOutchatRoom(Long memberId, String roomId);

    public List<ChatMessageDto> getChatMessages(String roomId);

    List<MemberChatroomSubInfo> sendDirectMessage(Long senderId, Long sendedId);

    DirectMessageRoom findDMRoomByRoomId(String roomId);
}
