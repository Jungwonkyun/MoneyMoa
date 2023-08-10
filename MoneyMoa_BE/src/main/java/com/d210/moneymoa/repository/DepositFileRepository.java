package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.DepositFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositFileRepository extends JpaRepository<DepositFile, Long> {
}
