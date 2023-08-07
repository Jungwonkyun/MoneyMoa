package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.DepositComment;
import com.d210.moneymoa.repository.DepositCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DepositCommentServiceImpl implements DepositCommentService {

    @Autowired
    private DepositCommentRepository depositCommentRepository;

    @Override
    @Transactional
    public void createDepositComment(String productCode, DepositComment depositComment) {
        depositComment.setProductCode(productCode);
        depositCommentRepository.save(depositComment);
    }

    @Override
    public List<DepositComment> findByProductCode(String productCode) {
        return depositCommentRepository.findByProductCode(productCode);
    }

    @Override
    public void deleteDepositComment(Long commentId, Long memberId) throws IllegalAccessException {
        DepositComment depositComment = depositCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!depositComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("You do not have permission to delete this comment.");
        }

        depositCommentRepository.deleteById(commentId);
    }

    @Override
    public void updateDepositComment(Long commentId, Long memberId, DepositComment updateDepositComment) throws IllegalAccessException {
        DepositComment depositComment = depositCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId: " + commentId));

        if (!depositComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("You do not have permission to update this comment.");
        }

        depositComment.setContent(updateDepositComment.getContent());
        depositCommentRepository.save(depositComment);
    }

}
