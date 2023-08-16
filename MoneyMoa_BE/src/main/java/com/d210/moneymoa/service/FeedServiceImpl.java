package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.dto.FeedLike;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.FeedLikeRepository;
import com.d210.moneymoa.repository.FeedRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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



    // 피드 생성
    @Override
    @Transactional
    public Feed createFeed(Long challengeId, Long memberId, Feed inputfeed) {

        Feed feed = Feed.builder()
                .memberId(memberId)
                .content(inputfeed.getContent())
                .challengeId(challengeId)
                .hashtag(inputfeed.getHashtag())
                .depositAmount(inputfeed.getDepositAmount())
//                .firstLikeCount(0) // 보기 위해 코드 저장. 08130440
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

    // feed 상세 조회
    @Override
    public Feed getFeedDetail(Long feedId) {
        // Feed 찾기
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new NoSuchElementException("Feed with id " + feedId + " not found")
        );
        if(feed.getChallenge() != null){
            feed.setChallengeTitle(feed.getChallenge().getTitle());
        }

        return feed;
    }





    @Override
    public Feed updateFeed(Long feedId, Feed updateFeed, Long memberId) throws IllegalAccessException {
        Feed originalFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물이 존재하지 않습니다."));

        if (!originalFeed.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("게시물의 작성자만 수정할 수 있습니다.");
        }

        if (updateFeed.getContent() != null) {
            originalFeed.setContent(updateFeed.getContent());
        }

        if (updateFeed.getChallengeId() != null) {
            originalFeed.setChallengeId(updateFeed.getChallengeId());
        }

        if (updateFeed.getHashtag() != null) {
            originalFeed.setHashtag(updateFeed.getHashtag());
        }

        if (updateFeed.getDepositAmount() != null) {
            originalFeed.setDepositAmount(updateFeed.getDepositAmount());
        }

        feedRepository.save(originalFeed);

        // 반환 전에 원래 피드의 depositAmount를 반환합니다.
        return originalFeed;
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



    @Override
    public Feed findById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new NoSuchElementException("해당 피드가 존재하지 않습니다."));
    }


    // 피드 좋아요 관련 코드들 --------------------------------------------------
    public boolean likeFeed(Long feedId, Long memberId) {
        // 피드Id와 memberId로 이미 Like 객체가 있는지 확인
        Optional<FeedLike> optionalLike = feedLikeRepository.findByFeedIdAndMemberId(feedId, memberId);

        if (!optionalLike.isPresent()) {
            // Like 객체가 없을 경우, 새로 생성해서 저장
            Optional<Feed> optionalFeed = feedRepository.findById(feedId);
            Optional<Member> optionalMember = memberRepository.findById(memberId);

            if (optionalFeed.isPresent() && optionalMember.isPresent()) {
                FeedLike newLike = FeedLike.builder()
                        .feed(optionalFeed.get())
                        .member(optionalMember.get())
                        .build();
                feedLikeRepository.save(newLike);
                return true;
            }
        }

        return false;

    }

    public boolean unlikeFeed(Long feedId, Long memberId) {
        // 피드Id와 memberId로 이미 Like 객체가 있는지 확인
        Optional<FeedLike> optionalLike = feedLikeRepository.findByFeedIdAndMemberId(feedId, memberId);

        if (optionalLike.isPresent()) {
            // Like 객체가 있을 경우, 삭제
            feedLikeRepository.delete(optionalLike.get());
            return true;
        } else {
            return false;
        }

    }

}


