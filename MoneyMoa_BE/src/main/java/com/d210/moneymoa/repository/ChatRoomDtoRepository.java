package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.ChatRoomDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomDtoRepository extends JpaRepository<ChatRoomDto, Long> {

    Optional<ChatRoomDto> findByRoomId(String roomID);

}
