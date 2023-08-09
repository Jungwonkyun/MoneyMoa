package com.d210.moneymoa.repository;



import com.d210.moneymoa.dto.ChatMessageDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageDtoRepository extends MongoRepository<ChatMessageDto, String> {

    List<ChatMessageDto> findByRoomId(String roomId);
}
