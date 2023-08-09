package com.d210.moneymoa.dto.FeedLikeDto;

import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.feed.Feed;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FeedLike {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

//    private Long memberId;
//    private Long feedId;

    @Builder
    public FeedLike(Member member, Feed feed) {
        this.member = member;
        this.feed = feed;
    }
//    @Builder
//    public FeedLikeRequest(Long memberId, Long feedId) {
//        this.memberId = memberId;
//        this.feedId = feedId;
//    }

}
