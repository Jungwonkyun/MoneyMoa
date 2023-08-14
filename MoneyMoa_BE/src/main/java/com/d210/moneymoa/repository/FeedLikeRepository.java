package com.d210.moneymoa.repository;


import com.d210.moneymoa.dto.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {


    Optional<FeedLike> findByMemberIdAndFeedId(Long memberId,Long feedId);


    List<FeedLike> findAllByFeedId(Long feedId);

}
