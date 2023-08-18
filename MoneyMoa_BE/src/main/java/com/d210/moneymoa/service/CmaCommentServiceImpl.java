package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.CmaComment;
import com.d210.moneymoa.repository.CmaCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmaCommentServiceImpl implements CmaCommentService {

    @Autowired
    private CmaCommentRepository cmaCommentRepository;

    @Override
    public void createCmaComment(CmaComment cmaComment) {
        cmaCommentRepository.save(cmaComment);
    }
    @Override
    public List<CmaComment> getCmaCommentsByCmaId(Long cmaId) {
        return cmaCommentRepository.findAllByCmaId(cmaId);
    }
    @Override
    public void deleteCmaComment(Long commentId, Long memberId) throws IllegalAccessException {
        CmaComment cmaComment = cmaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!cmaComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("You do not have permission to delete this comment.");
        }

        cmaCommentRepository.delete(cmaComment);
    }

    @Override
    public void updateCmaComment(Long commentId, Long memberId, CmaComment updatedCmaComment) throws IllegalAccessException {
        CmaComment cmaComment = cmaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!cmaComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("You do not have permission to update this comment.");
        }

        cmaComment.setContent(updatedCmaComment.getContent());
        cmaCommentRepository.save(cmaComment);
    }
}
