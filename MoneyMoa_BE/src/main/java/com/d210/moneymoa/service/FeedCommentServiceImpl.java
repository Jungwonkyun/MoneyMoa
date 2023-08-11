package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.FeedComment;
import com.d210.moneymoa.repository.FeedCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FeedCommentServiceImpl implements FeedCommentService {

    @Autowired
    private FeedCommentRepository feedCommentRepository;


    @Override
    @Transactional
    public void createFeedComment(Long feedId, FeedComment feedComment) {
        feedComment.setFeedId(feedId);
        feedCommentRepository.save(feedComment);
    }

    @Override
    public List<FeedComment> findByFeedId(Long feedId) {
        return feedCommentRepository.findByFeedId(feedId);
    }

    @Override
    public void updateFeedComment(Long commentId, Long memberId, FeedComment editFeedComment) throws IllegalAccessException {
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId : " + commentId));

        if (!feedComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("댓글을 수정할 권한이 없습니다.");
        }
        feedComment.setContent(editFeedComment.getContent());
        feedCommentRepository.save(feedComment);
    }

    @Override
    public void deleteFeedComment(Long commentId, Long memberId) throws IllegalAccessException {
        FeedComment feedComment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid commentId : " + commentId));

        if (!feedComment.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("댓글을 삭제할 권한이 없습니다.");
        }

        feedCommentRepository.deleteById(commentId);
    }















}
