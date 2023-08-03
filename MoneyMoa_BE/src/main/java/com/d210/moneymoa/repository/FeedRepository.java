package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {


    Optional<Feed> findById(Long id);
}
