//package com.d210.moneymoa.service;
//
//import com.d210.moneymoa.Exception.DuplicateResourceException;
//import com.d210.moneymoa.dto.FeedLikeDto.FeedLike;
//import com.d210.moneymoa.dto.FeedLikeDto.FeedLikeRequest;
//import com.d210.moneymoa.dto.Member;
//import com.d210.moneymoa.dto.Feed;
//import com.d210.moneymoa.repository.FeedLikeRepository;
//import com.d210.moneymoa.repository.FeedRepository;
//import com.d210.moneymoa.repository.MemberRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class FeedLikeServiceImpl implements FeedLikeService {
//
//    @Autowired
//    FeedLikeRepository feedLikeRepository;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    FeedRepository feedRepository;
//    @Autowired
//    FeedService feedService;
//
//    @Transactional
//    public void insert(FeedLikeRequest feedLikeReqest) {
//
//        Member member = memberRepository.findById(feedLikeReqest.getMemberId())
//                .orElseThrow(() -> new EmptyResultDataAccessException("Could not found member id : " + feedLikeReqest.getMemberId(), 1));
//
//        Feed feed = feedRepository.findById(feedLikeReqest.getFeedId())
//                .orElseThrow(() -> new EmptyResultDataAccessException("Could not found feed id : " + feedLikeReqest.getFeedId(), 1));
//
//        //이미 좋아요 했으면 에러 반환
//        if (feedLikeRepository.findByMemberAndFeed(member, feed).isPresent()) {
//            throw new DuplicateResourceException("already exist data by member id :" + member.getId() + " ,"
//                    + "feed id : " + feed.getId());
//        }
//        FeedLike feedLike = FeedLike.builder()
//                .feed(feed)
//                .member(member)
//                .build();
//
//        feedLikeRepository.save(feedLike);
//        feedService.addLikeCount(feed.getId());
//    }
//
//    @Transactional
//    public void delete(FeedLikeRequest feedLikeRequest) {
//
//        Member member = memberRepository.findById(feedLikeRequest.getMemberId())
//                .orElseThrow(() -> new EmptyResultDataAccessException("Could not found member id : " + feedLikeRequest.getMemberId(), 1));
//
//        Feed feed = feedRepository.findById(feedLikeRequest.getFeedId())
//                .orElseThrow(() -> new EmptyResultDataAccessException("Could not found feed id : " + feedLikeRequest.getFeedId(), 1));
//
//        FeedLike feedLike = feedLikeRepository.findByMemberAndFeed(member, feed)
//                .orElseThrow(() -> new EmptyResultDataAccessException("Could not found feedLike id", 1));
//
//        feedLikeRepository.delete(feedLike);
//        feedService.subLikeCount(feed.getId());
//    }
//}
