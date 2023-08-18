package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.CmaComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CmaCommentRepository extends JpaRepository<CmaComment, Long> {
    List<CmaComment> findAllByCmaId(Long cmaId);
}
