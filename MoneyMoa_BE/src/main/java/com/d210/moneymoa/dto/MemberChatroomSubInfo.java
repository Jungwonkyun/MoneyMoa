package com.d210.moneymoa.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@Document(collection = "subscribe_info") // MongoDB의 컬렉션 이름을 지정합니다.
@NoArgsConstructor
public class MemberChatroomSubInfo implements Serializable {

    @Id
    private String id; //
    private long memberId; // 멤버 Id정보
    private String memberNickname; //멤버 닉네임
    private String roomId; // 메시지

    @Builder
    public MemberChatroomSubInfo(long memberId, String memberNickname, String roomId) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.roomId = roomId;
    }
}
