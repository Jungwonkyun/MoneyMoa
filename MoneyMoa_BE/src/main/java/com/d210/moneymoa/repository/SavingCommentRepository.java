package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.SavingComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingCommentRepository extends JpaRepository<SavingComment, Long> {
    List<SavingComment> findByProductCode(String productCode);
}
