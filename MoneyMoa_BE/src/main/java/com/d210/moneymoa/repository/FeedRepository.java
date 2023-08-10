package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findById(Long id);
    List<Feed> findAllByMemberId(Long memberId);
    List<Feed> findByMemberId(Long memberId);


}
