package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Feed;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FeedService {


    Feed createFeed(Long challengeId, Long memberId, Feed feed);

    List<Feed> getAllFeeds() throws InterruptedException;

    List<Feed> getMemberFeeds(Long memberId);

    Feed getFeedDetail(Long feedId);

    Integer updateFeed(Long feedId, Feed updateFeed, Long memberId) throws IllegalAccessException;

    void deleteFeed(Long feedId, Long memberId) throws IllegalAccessException;

    List<Feed> findByHashtags(String hashtag);
    List<Feed> findByContent(String content);


// 피드 좋아요
    Feed getFeedById(Long feedId);

    boolean toggleLike(Long memberId, Long feedId);
//    void increaseFeedLikeCount(Long feedId);
//    void decreaseFeedLikeCount(Long feedId);




}
