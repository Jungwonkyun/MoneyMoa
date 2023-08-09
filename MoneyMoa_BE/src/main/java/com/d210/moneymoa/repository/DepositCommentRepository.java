package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.DepositComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositCommentRepository extends JpaRepository<DepositComment, Long> {
    List<DepositComment> findByProductCode(String productCode);
}
