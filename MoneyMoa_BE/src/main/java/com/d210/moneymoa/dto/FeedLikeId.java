package com.d210.moneymoa.dto;

import java.io.Serializable;
import java.util.Objects;

public class FeedLikeId implements Serializable {

    private Long memberId;
    private Long feedId;

    public FeedLikeId() {
    }

    public FeedLikeId(Long memberId, Long feedId) {
        this.memberId = memberId;
        this.feedId = feedId;
    }

    // equals()와 hashCode() 메서드는 중요합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedLikeId that = (FeedLikeId) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(feedId, that.feedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, feedId);
    }

}
