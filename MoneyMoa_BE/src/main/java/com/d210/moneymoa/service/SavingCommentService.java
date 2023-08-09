package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.SavingComment;

import java.util.List;

public interface SavingCommentService {
    void createSavingComment(String productCode, SavingComment savingComment);

    List<SavingComment> findByProductCode(String productCode);

    void deleteSavingComment(Long commentId, Long memberId) throws IllegalAccessException;

    void updateSavingComment(Long commentId, Long memberId, SavingComment updateSavingComment) throws IllegalAccessException;
}
