package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.LikedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedDepositRepository extends JpaRepository<LikedDeposit, Long> {
}
