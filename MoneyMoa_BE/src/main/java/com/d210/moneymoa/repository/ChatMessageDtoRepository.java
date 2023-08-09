package com.d210.moneymoa.repository;



import com.d210.moneymoa.dto.ChatMessageDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageDtoRepository extends MongoRepository<ChatMessageDto, String> {

}
