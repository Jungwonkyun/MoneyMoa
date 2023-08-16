package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.ChatRoomDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface ChatRoomDtoRepository extends MongoRepository<ChatRoomDto, String> {

    Optional<ChatRoomDto> findByRoomId(String roomId);

    List<ChatRoomDto> findByNameContaining(String name);

    Optional<ChatRoomDto> findByName(String name);


}
