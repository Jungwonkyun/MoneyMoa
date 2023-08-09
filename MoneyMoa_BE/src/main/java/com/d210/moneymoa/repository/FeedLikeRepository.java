package com.d210.moneymoa.repository;


import com.d210.moneymoa.dto.FeedLikeDto.FeedLike;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    Optional<FeedLike> findByMemberAndFeed(Member member, Feed feed);



}
