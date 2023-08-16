package com.d210.moneymoa.repository;


import com.d210.moneymoa.dto.MemberChatroomSubInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberChatroomSubInfoRepository extends MongoRepository<MemberChatroomSubInfo, String> {

    List<MemberChatroomSubInfo> findByRoomId(String roomId);
    Optional<MemberChatroomSubInfo> findByMemberIdAndRoomId(long memberId, String roomId);
}
