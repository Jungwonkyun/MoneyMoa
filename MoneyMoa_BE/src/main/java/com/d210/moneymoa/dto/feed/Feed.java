package com.d210.moneymoa.dto.feed;

import com.d210.moneymoa.dto.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    // 작성자, 내용, 챌린지 선택, 해시태그, 납입 금액, 이미지
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 내용
    private String challenge; // 챌린지 종류
    private String hashtag; // 해쉬태그
    private Integer depositAmount; // 납입 금액


//    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createDateTime; // 날짜
    @PrePersist
    public void createDateTime() {
        this.createDateTime = LocalDateTime.now();
    }

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member; //id


    @ColumnDefault("0")
    private Integer feedLikeCount;

    public void setLikeCount(int feedLikeCount) {
        this.feedLikeCount = feedLikeCount;
    }
    public Integer getLikeCount() {
        return feedLikeCount;
    }


    @Builder
    public Feed(Long memberId, String content, String challenge,
                String hashtag, Integer depositAmount) {
        this.memberId = memberId;
        this.content = content;
        this.challenge = challenge;
        this.hashtag = hashtag;
        this.depositAmount = depositAmount;
    }

}
