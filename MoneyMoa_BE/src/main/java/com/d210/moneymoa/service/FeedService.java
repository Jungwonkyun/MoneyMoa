package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Feed;
import org.springframework.stereotype.Service;

@Service
public interface FeedService {


    Feed createFeed(Long challengeId, Long memberId, Feed feed);

    /*
    List<Feed> getAllFeeds(Long memberId);
    List<Feed> getAllFeedsForMember(Long memberId);
    Feed updateFeed(Long id, Feed req, Long memberId, String jwt) throws AuthorizationException;

    Feed findFeed(Long id);



    void deleteFeed(Long id, String jwt) throws AuthorizationException;


    //    댓글 카운트
    void addLikeCount(Long feedId);

    void subLikeCount(Long feedId);

     */

}
