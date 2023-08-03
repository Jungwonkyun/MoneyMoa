package com.d210.moneymoa.dto.feed;

import com.d210.moneymoa.dto.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity
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

    // Many-to-One 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // Lazy 로딩 사용 (필요 시에만 로딩)
    @JoinColumn(name = "member_id") // Member 엔티티의 PK와 연결되는 컬럼명
    private Member member;


    @Column
    private String nickname;


//    // Cascade
//    @OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private List<FeedImg> feedImg;

    @Builder
    public Feed(String content, String challenge,
                String hashtag, Integer depositAmount) {
        this.content = content;
        this.challenge = challenge;
        this.hashtag = hashtag;
        this.depositAmount = depositAmount;
        this.createDateTime = createDateTime;

//        this.feedImg = feedImg;
    }

//    public ImageUpdatedResult update()


}
