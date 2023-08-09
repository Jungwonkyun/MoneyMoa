package com.d210.moneymoa.dto.feed;

import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.challenge.Challenge;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter @Setter
public class FeedCreateResponse {

    private Long id;
    private String content;

//    @ApiModelProperty(value = "챌린지 선택", notes = "챌린지를 선택해주세요.", required = true, example = "String")
//    @Column(nullable = false)
//    private Long challengeId; // 챌린지 종류
//
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "challengeId", referencedColumnName = "id", insertable = false, updatable = false)
//    private Challenge challenge; //id

    @JsonIgnore
    private Challenge challenge;

    private String hashtag;

    private LocalDateTime createDateTime;

    private Integer depositAmount;

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member; //id



    public FeedCreateResponse(Long id, Long memberId, String content, Challenge challenge,
                              String hashtag, LocalDateTime createDateTime,
                              Integer depositAmount) {
        this.id = id;
        this.memberId = memberId;
        this.content = content;
        this.challenge = challenge;
        this.hashtag = hashtag;
        this.createDateTime = createDateTime;
        this.depositAmount = depositAmount;
    }
    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    public static FeedCreateResponse toDto(Feed feed) {
        return new FeedCreateResponse(
                feed.getId(),
                feed.getMemberId(),
                feed.getContent(),
                feed.getChallenge(),
                feed.getHashtag(),
                feed.getCreateDateTime(),
                feed.getDepositAmount()
        );
    }

}
