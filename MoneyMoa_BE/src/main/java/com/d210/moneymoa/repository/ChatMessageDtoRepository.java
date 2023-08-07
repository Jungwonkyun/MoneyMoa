package com.d210.moneymoa.repository;



import com.d210.moneymoa.dto.ChatMessageDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageDtoRepository extends JpaRepository<ChatMessageDto, Long> {

}
