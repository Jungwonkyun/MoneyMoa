package com.d210.moneymoa.repository;


import com.d210.moneymoa.dto.DirectMessageRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Optional;

public interface DirectMessageRoomRepository extends MongoRepository<DirectMessageRoom, String> {

    List<DirectMessageRoom>findAllDirectMessageRoomBySenderIdOrReceiverId(Long senderId, Long receiverId);

    Optional<DirectMessageRoom> findByRoomId(String roomId);

    List<DirectMessageRoom> findBySenderId(Long senderId);
    List<DirectMessageRoom> findByReceiverId(Long receiverId);
}
