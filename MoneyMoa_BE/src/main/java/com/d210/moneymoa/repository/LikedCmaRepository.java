package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.LikedCma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedCmaRepository extends JpaRepository<LikedCma, Long> {
    List<LikedCma> findAllByMemberId(Long memberId);

    void deleteByMemberIdAndId(Long memberId, Long id);
}
