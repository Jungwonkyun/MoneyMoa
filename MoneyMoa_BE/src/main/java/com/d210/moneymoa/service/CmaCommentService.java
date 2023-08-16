package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.CmaComment;

import java.util.List;

public interface CmaCommentService {
    void createCmaComment(CmaComment cmaComment);

    List<CmaComment> getCmaCommentsByCmaId(Long cmaId);

    void deleteCmaComment(Long commentId, Long memberId) throws IllegalAccessException;

    void updateCmaComment(Long commentId, Long memberId, CmaComment updatedCmaComment) throws IllegalAccessException;
}
