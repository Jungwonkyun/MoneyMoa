package com.d210.moneymoa.dto.feed;

import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.challenge.Challenge;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {

//    private Long challenge; //id

    private String content; // 내용

//

    private Challenge challenge; // 챌린지 종류 값을 저장할 필드 추가

    private Long challengeId; // 챌린지 종류

    private String hashtag; // 해쉬태그

    private Integer depositAmount; // 납입 금액



//    private Integer likeCount;

    //    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createDateTime; // 날짜

    @PrePersist
    public void createDateTime() {
        this.createDateTime = LocalDateTime.now();
    }

    private Long memberId;

    private Member member; //id

    public FeedResponse(String content, Challenge challenge, String hashtag, Integer depositAmount,
                        LocalDateTime createDateTime, Long memberId, Member member) {
        this.content = content;
        this.challenge = challenge;
        this.hashtag = hashtag;
        this.depositAmount = depositAmount;
        this.createDateTime = createDateTime;
        this.memberId = memberId;
        this.member = member;
    }


    //    @Builder
    public static FeedResponse toDto(Feed feed) {

        return new FeedResponse(
//                feed.getId(),
                feed.getContent(),
                feed.getChallenge(),
                feed.getHashtag(),
                feed.getDepositAmount(),
                feed.getCreateDateTime(),
                feed.getMemberId(),
                feed.getMember()
        );
    }
}

