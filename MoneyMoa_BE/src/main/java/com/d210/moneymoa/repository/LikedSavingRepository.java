package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.LikedSaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedSavingRepository extends JpaRepository<LikedSaving, Long> {
    List<LikedSaving> findAllByMemberId(Long memberId);

    void deleteByMemberIdAndId(Long memberId, Long id);
}

