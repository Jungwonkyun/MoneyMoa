package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.ChatRoomDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface ChatRoomDtoRepository extends MongoRepository<ChatRoomDto, String> {

    Optional<ChatRoomDto> findByRoomId(String roomId);

    Optional<ChatRoomDto> findByName(String name);


}
