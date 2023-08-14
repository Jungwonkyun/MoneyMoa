package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.dto.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FeedService {


    Feed createFeed(Long challengeId, Long memberId, Feed feed);

    List<Feed> getAllFeeds() throws InterruptedException;

    List<Feed> getMemberFeeds(Long memberId);

    Feed getFeedDetail(Long feedId);

    Feed updateFeed(Long feedId, Feed updateFeed, Long memberId) throws IllegalAccessException;

    void deleteFeed(Long feedId, Long memberId) throws IllegalAccessException;

    List<Feed> findByHashtags(String hashtag);
    List<Feed> findByContent(String content);


// 피드 좋아요
    Feed getFeedById(Long feedId);
//
//    boolean toggleLike(Long memberId, Long feedId);
//
//    List<Long> getLikersMemberIds(Long feedId);
//
    Feed findById(Long feedId);    // 메서드 추가


}
