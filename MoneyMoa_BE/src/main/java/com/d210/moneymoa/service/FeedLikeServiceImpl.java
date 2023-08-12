//package com.d210.moneymoa.service;
//
//import com.d210.moneymoa.dto.Feed;
//import com.d210.moneymoa.dto.FeedLike;
//import com.d210.moneymoa.repository.FeedLikeRepository;
//import com.d210.moneymoa.repository.FeedRepository;
//import org.checkerframework.checker.units.qual.A;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class FeedLikeServiceImpl implements FeedLikeService {
//
//    @Autowired
//    FeedLikeRepository feedLikeRepository;
//
//    @Autowired
//    FeedRepository feedRepository;
////
////    @Autowired
////    public FeedLikeServiceImpl(FeedLikeRepository likeRepository) {
////        this.feedLikeRepository = likeRepository;
////    }
////
////    @Override
////    public boolean toggleLike(Long memberId, Long feedId) {
////        Optional<FeedLike> like = feedLikeRepository.findByMemberAndFeed(memberId, feedId);
////        if (like.isPresent()) {
////            feedLikeRepository.delete(like.get());
////            return false;
////        } else {
////            feedLikeRepository.save(new FeedLike(memberId, feedId));
////            return true;
////        }
//
//
//        @Override
//        public void increaseFeedLikeCount(Long feedId) {
//            Feed feed = getFeedById(feedId);
//            feed.increaseFeedLikeCount();
//            feedRepository.save(feed);
//        }
//
//        @Override
//        public void decreaseFeedLikeCount(Long feedId) {
//            Feed feed = getFeedById(feedId);
//            feed.decreaseFeedLikeCount();
//            feedRepository.save(feed);
//        }
//
//        @Override
//        Feed getFeedById(Long feedId);
//        return feedRepository.findByContentContaining(content);
//    }
//
//
//}
