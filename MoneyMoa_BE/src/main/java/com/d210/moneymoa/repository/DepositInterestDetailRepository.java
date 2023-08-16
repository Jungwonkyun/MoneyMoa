package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.DepositInterestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositInterestDetailRepository extends JpaRepository<DepositInterestDetail, Long> {
}
