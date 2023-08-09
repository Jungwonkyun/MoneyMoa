package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.DepositComment;

import java.util.List;

public interface DepositCommentService {
    void createDepositComment(String productCode, DepositComment depositComment);

    List<DepositComment> findByProductCode(String productCode);

    void deleteDepositComment(Long commentId, Long memberId) throws IllegalAccessException;

    void updateDepositComment(Long commentId, Long memberId, DepositComment updateDepositComment) throws IllegalAccessException;
}
