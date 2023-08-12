package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.dto.FeedLike;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.FeedLikeRepository;
import com.d210.moneymoa.repository.FeedRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    AuthTokensGenerator authTokensGenerator;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    FeedLikeRepository feedLikeRepository;

    public FeedServiceImpl(FeedRepository feedRepository, FeedLikeRepository feedLikeRepository) {
        this.feedRepository = feedRepository;
        this.feedLikeRepository = feedLikeRepository;
    }


    // 피드 생성
    @Override
    @Transactional
    public Feed createFeed(Long challengeId, Long memberId, Feed inputfeed) {

        Feed feed = Feed.builder()
                .content(inputfeed.getContent())
                .challengeId(challengeId)
                .hashtag(inputfeed.getHashtag())
                .depositAmount(inputfeed.getDepositAmount())
                .memberId(memberId)
                .feedLikeCount(inputfeed.getFeedLikeCount())
                .feedLikeCount(inputfeed.getFeedLikeCount())
                .build();

        String nickname = memberRepository.findById(memberId).get().getNickname();
        feed.setNickname(nickname);

        feedRepository.save(feed);
        return feed;
    }


    // 피드 전체 조회
    @Transactional(readOnly = true)
    public List<Feed> getAllFeeds() throws InterruptedException {
//        List<Feed> feeds = feedRepository.findAllByMemberId();
//        List<Feed> feedResponses = new ArrayList<>();
//        // feeds 리스트의 각 Feed 객체를 FeedCreateResponse로 변환하여 feedResponses 리스트에 저장
//        feeds.forEach(feed -> feedResponses.add(Feed(feed)));
//        // 변환된 피드 목록을 반환
        return feedRepository.findAll();
    }

    @Override
    public List<Feed> getMemberFeeds(Long memberId) {
        return feedRepository.findByMemberId(memberId);
    }

    @Override
    public Feed getFeedDetail(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(
                () -> new NoSuchElementException("Feed with id " + feedId + " not found")
        );
    }

    @Override
    public void updateFeed(Long feedId, Feed updateFeed, Long memberId) throws IllegalAccessException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("챌린지를 찾을 수 없습니다."));

        if (!feed.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("수정 권한이 없습니다.");
        }

        if (updateFeed.getContent() != null) {
            feed.setContent(updateFeed.getContent());
        }
        if (updateFeed.getChallengeId() != null) {
            feed.setChallengeId(updateFeed.getChallengeId());
        }
        if (updateFeed.getHashtag() != null) {
            feed.setHashtag(updateFeed.getHashtag());
        }
        if (updateFeed.getDepositAmount() != null) {
            feed.setDepositAmount(updateFeed.getDepositAmount());
        }
        feedRepository.save(feed);
    }

    @Override
    public void deleteFeed(Long feedId, Long memberId) throws IllegalAccessException {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("챌린지를 찾을 수 없습니다."));

        if (!feed.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("삭제 권한이 없습니다.");
        }
        // 피드 삭제
        feedRepository.deleteById(feedId);
    }

    @Override
    public List<Feed> findByHashtags(String hashtag) {
        return feedRepository.findByHashtagContaining(hashtag);
    }

    @Override
    public List<Feed> findByContent(String content) {
        return feedRepository.findByContentContaining(content);
    }

    @Override
    public Feed getFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("Feed with id " + feedId + " not found"));
    }


public boolean toggleLike(Long memberId, Long feedId) {
    Optional<FeedLike> like = feedLikeRepository.findByMemberIdAndFeedId(memberId, feedId);
    Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new NoSuchElementException("해당 피드가 존재하지 않습니다."));
    if (like.isPresent()) {
        feedLikeRepository.delete(like.get());
        feed.decreaseFeedLikeCount();
        feedRepository.save(feed);
        return false;
    } else {
        feedLikeRepository.save(new FeedLike(memberId, feedId));
        feed.increaseFeedLikeCount();
        feedRepository.save(feed);
        return true;
    }
    }
}


