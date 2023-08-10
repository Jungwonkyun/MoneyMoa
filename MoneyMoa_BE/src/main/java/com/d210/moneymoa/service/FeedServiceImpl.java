package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.FeedRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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


    // 피드 생성
    @Override
    @Transactional
    public Feed createFeed(Long challengeId, Long memberId,Feed inputfeed) {

       Feed feed = Feed.builder()
               .content(inputfeed.getContent())
               .challengeId(challengeId)
               .hashtag(inputfeed.getHashtag())
               .depositAmount(inputfeed.getDepositAmount())
               .memberId(memberId)
               .feedLikeCount(inputfeed.getFeedLikeCount())
               .feedLikeCount(inputfeed.getFeedLikeCount())
               .build();

       String nickname =  memberRepository.findById(memberId).get().getNickname();
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
    public void updateFeed(Long feedId, Feed updateFeed) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new NoSuchElementException("Feed with id" + feedId + " Not Found")
        );
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
    public void deleteFeed(Long feedId) {
        feedRepository.findById(feedId).orElseThrow(
                () -> new NoSuchElementException("Feed with id " + feedId + " not found")
        );
        // 피드 삭제
        feedRepository.deleteById(feedId);
    }

//
//    // 특정 회원의 피드 전체 조회
//    @Transactional(readOnly = true)
//    public List<Feed> getAllFeedsForMember(Long memberId) {
//        List<Feed> feeds = feedRepository.findAllByMemberId(memberId);
//        return feeds.stream()
//                .map(Feed::toDto)
//                .collect(Collectors.toList());
//    }
//
//
//    // 피드 상세 조회
//    @Transactional(readOnly = true)
//    public Feed findFeed(final Long id) {
//        Optional<Feed> oFeed = feedRepository.findById(id);
//        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));
//
//        return Feed.toDto(feed);
//    }
//
//
//    //throws AuthorizationException 삭제 했을 때.
//    @Transactional
//    public Feed updateFeed(Long id, Feed req, Long memberId, String jwt) throws AuthorizationException {
//        Optional<Feed> oFeed = feedRepository.findById(id);
//        Optional<Member> oMember = memberRepository.findById(memberId);
//
//        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));
//        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("삭제 할 권한이 없습니다. id: " + memberId));
//
//        if (member.getId().equals(feed.getMemberId())) {
//            feed.setContent(req.getContent());
//            feed.setChallengeId(req.getChallengeId());
//            feed.setHashtag(req.getHashtag());
//            feed.setDepositAmount(req.getDepositAmount());
//
//            Feed updatedFeed = feedRepository.save(feed);
//            return Feed.toDto(updatedFeed);
//        } else {
//            throw new AuthorizationException("권한이 없습니다.");
//        }
//    }
//
//    @Transactional
//    public void addLikeCount(Long feedId) {
//        Optional<Feed> oFeed = feedRepository.findById(feedId);
//        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + feedId));
//        feed.setLikeCount(feed.getLikeCount() + 1);
//        feedRepository.save(feed);
//    }
//
//    @Transactional
//    public void subLikeCount(Long feedId) {
//        Optional<Feed> oFeed = feedRepository.findById(feedId);
//        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + feedId));
//
//        feed.setLikeCount(Math.max(feed.getLikeCount() - 1, 0));
//        feedRepository.save(feed);
//    }
//
//
//    // 피드 삭제 메소드
//    @Transactional
//    public void deleteFeed(Long id, @RequestHeader("Authorization") String jwt) { // 불필요한 인자 제거
//        // JWT 토큰에서 사용자 ID 추출
//        Long memberId = authTokensGenerator.extractMemberId(jwt);
//        Optional<Feed> oFeed = feedRepository.findById(id);
//        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));
//
//        log.info(String.valueOf(feed.getMemberId()));
//
//        if (!feed.getMemberId().equals(memberId)) { // 피드 작성자의 멤버 ID와 현재 멤버의 ID 비교
//            throw new UnauthorizedException("게시글 삭제 권한이 없습니다.");
//        }
//
//        feedRepository.deleteById(id);
//    }

}
