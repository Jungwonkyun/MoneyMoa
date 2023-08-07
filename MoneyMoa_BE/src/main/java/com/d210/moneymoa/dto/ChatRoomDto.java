package com.d210.moneymoa.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoomDto implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
