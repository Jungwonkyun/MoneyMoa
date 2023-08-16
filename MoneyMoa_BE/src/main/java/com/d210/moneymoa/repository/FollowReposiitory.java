package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.Follows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowReposiitory extends JpaRepository<Follows, Long> {
    Optional<Follows> findByFromMemberIdAndToMemberId(Long fromMemberId, Long toMemberId);

    List<Follows> findByFromMemberId(Long fromMemberId);

    List<Follows> findByToMemberId(Long toMemberId);
}
