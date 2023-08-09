package com.d210.moneymoa.dto.feed;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content; // 내용
    private String challenge; // 챌린지 종류
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



    @Builder
    public static FeedResponse toDto(Feed feed) {

        return new FeedResponse(
                feed.getId(),
                feed.getContent(),
                feed.getChallenge(),
                feed.getHashtag(),
                feed.getDepositAmount(),
                feed.getCreateDateTime()
                );

    }
    }

