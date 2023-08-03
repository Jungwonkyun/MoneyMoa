package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.feed.FeedCreateRequest;
import com.d210.moneymoa.dto.feed.FeedCreateResponse;
import com.d210.moneymoa.dto.feed.FeedResponse;
import com.d210.moneymoa.dto.feed.FeedUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FeedService {


    FeedCreateResponse createFeed(FeedCreateRequest req, Long memberId);

    List<FeedCreateResponse> getAllFeeds();

    FeedCreateResponse editFeed(Long id, FeedUpdateRequest req, String jwt);

    FeedResponse findFeed(Long id);




}
