package com.d210.moneymoa.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "chat_rooms") // MongoDB의 컬렉션 이름을 지정합니다.
@NoArgsConstructor
public class ChatRoomDto implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    private String id; // MongoDB는 주로 문자열 형식의 ID를 사용합니다. (예: ObjectId)
    private String roomId;
    private String name;
    private String description;
    private String imgUrl;

    @Builder
    public ChatRoomDto(String roomId, String name, String description, String imgUrl) {
        this.roomId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
    }
}

