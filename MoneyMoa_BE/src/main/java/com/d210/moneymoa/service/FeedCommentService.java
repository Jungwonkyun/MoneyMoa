package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.dto.FeedComment;

import java.util.List;

public interface FeedCommentService {

    void createFeedComment(Long feedId, FeedComment feedComment);

    List<FeedComment> findByFeedId(Long feedId);

    void updateFeedComment(Long commentId, Long memberId, FeedComment editFeedComment) throws IllegalAccessException;
    void deleteFeedComment(Long commentId, Long memberId) throws IllegalAccessException;
}
