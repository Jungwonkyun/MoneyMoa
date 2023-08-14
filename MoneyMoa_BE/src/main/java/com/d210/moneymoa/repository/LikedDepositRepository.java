package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.LikedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedDepositRepository extends JpaRepository<LikedDeposit, Long> {
    List<LikedDeposit> findAllByMemberId(Long memberId);
}
