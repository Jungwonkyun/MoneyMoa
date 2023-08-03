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
    private String nickname;

//    private int likeCount;
//    private int comment;

//    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createDateTime; // 날짜
    @PrePersist
    public void createDateTime() {
        this.createDateTime = LocalDateTime.now();
    }


    // Many-to-One 관계 설정
//    @ManyToOne(fetch = FetchType.LAZY) // Lazy 로딩 사용 (필요 시에만 로딩)
//    @JoinColumn(name = "member_id") // Member 엔티티의 PK와 연결되는 컬럼명
//    private Member member;

//    public FeedResponse(Long id,
//                        String content,
//                        String challenge,
//                        String hashtag,
//                        Integer depositAmount,
//                        LocalDateTime createDateTime,
//                        String nickname) {
//        this.id = id;
//        this.content = content;
//        this.challenge = challenge;
//        this.hashtag = hashtag;
//        this.depositAmount = depositAmount;
//        this.createDateTime = createDateTime;
//        this.nickname = nickname;
//    }

    @Builder
    public static FeedResponse toDto(Feed feed) {
        return new FeedResponse(
                        feed.getId(),
                        feed.getContent(),
                        feed.getChallenge(),
                        feed.getHashtag(),
                        feed.getDepositAmount(),
                        feed.getNickname(),
                        feed.getCreateDateTime()
                );
    }



}
