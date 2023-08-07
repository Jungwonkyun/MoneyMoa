package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.DepositComment;

import com.d210.moneymoa.dto.SavingComment;
import com.d210.moneymoa.repository.SavingCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SavingCommentServiceImpl implements SavingCommentService {

    @Autowired
    private SavingCommentRepository savingCommentRepository;

    @Override
    @Transactional
    public void createSavingComment(String productCode, SavingComment savingComment) {
        savingComment.setProductCode(productCode);
        savingCommentRepository.save(savingComment);
    }

    @Override
    public List<SavingComment> findByProductCode(String productCode) {
        return savingCommentRepository.findByProductCode(productCode);
    }

    @Override
    public void deleteSavingComment(Long commentId, Long memberId) throws IllegalAccessException {
        SavingComment savingComment = savingCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!savingComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("You do not have permission to delete this comment.");
        }

        savingCommentRepository.deleteById(commentId);
    }

    @Override
    public void updateSavingComment(Long commentId, Long memberId, SavingComment updateSavingComment) throws IllegalAccessException {
        SavingComment savingComment = savingCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!savingComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("You do not have permission to update this comment.");
        }

        savingComment.setContent(updateSavingComment.getContent());
        savingCommentRepository.save(savingComment);
    }

}
