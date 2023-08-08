package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.AuthorizationException;
import com.d210.moneymoa.dto.feed.FeedCreateRequest;
import com.d210.moneymoa.dto.feed.FeedCreateResponse;
import com.d210.moneymoa.dto.feed.FeedResponse;
import com.d210.moneymoa.dto.feed.FeedUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public interface FeedService {


    FeedCreateResponse createFeed(FeedCreateRequest req,  Long memberId, String jwt);

    List<FeedCreateResponse> getAllFeeds(Long memberId);
    List<FeedCreateResponse> getAllFeedsForMember(Long memberId);
    FeedCreateResponse updateFeed(Long id, FeedUpdateRequest req, Long memberId, String jwt) throws AuthorizationException;

    FeedResponse findFeed(Long id);

    void deleteFeed(Long id, String jwt) throws AuthorizationException;


    //    댓글 카운트
    void addLikeCount(Long feedId);

    void subLikeCount(Long feedId);




}
