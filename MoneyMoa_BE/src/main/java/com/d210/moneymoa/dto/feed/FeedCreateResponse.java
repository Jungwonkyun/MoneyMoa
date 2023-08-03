package com.d210.moneymoa.dto.feed;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class FeedCreateResponse {

    private Long id;
    private String content;
    private String challenge;
    private String hashtag;
    private LocalDateTime createDateTime;
    private Integer depositAmount;
    private String nickname;



    public FeedCreateResponse(Long id, String content, String challenge,
                              String hashtag, LocalDateTime createDateTime, Integer depositAmount,
                              String nickname) {
        this.id = id;
        this.content = content;
        this.challenge = challenge;
        this.hashtag = hashtag;
        this.createDateTime = createDateTime;
        this.depositAmount = depositAmount;
        this.nickname = nickname;
    }
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    public static FeedCreateResponse toDto(Feed feed) {
        return new FeedCreateResponse(
                feed.getId(),
                feed.getContent(),
                feed.getChallenge(),
                feed.getHashtag(),
                feed.getCreateDateTime(),
                feed.getDepositAmount(),
                feed.getNickname()
        );
    }

}
