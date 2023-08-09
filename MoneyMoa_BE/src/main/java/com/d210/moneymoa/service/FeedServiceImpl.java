package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.*;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.challenge.Challenge;
import com.d210.moneymoa.dto.feed.*;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.FeedRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
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


    @Transactional
    public FeedCreateResponse createFeed(FeedCreateRequest req, Long memberId, String jwt, Long challengeId) {
        // 멤버 저장소에서 memberId를 사용하여 멤버 찾기
        Optional<Member> oMember = memberRepository.findById(memberId);
        // 멤버가 없으면 MemberNotFoundException 발생시키기
        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId));
        log.info(member.toString());

        // Challenge 저장소에서 challengeId를 사용하여 챌린지 찾기
        Optional<Challenge> oChallenge = challengeRepository.findById(challengeId);
        // 챌린지가 없으면 ChallengeNotFoundException 발생시키기
        Challenge challenge = oChallenge.orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다. id: " + challengeId));

        log.info(String.valueOf(memberId));

        // 요청 데이터를 사용하여 새로운 피드 객체 생성
        Feed feed = Feed.builder()
                .memberId(memberId)
                .content(req.getContent())
//                .challengeId(challengeId)
                .challenge(challenge)
                .hashtag(req.getHashtag())
                .depositAmount(req.getDepositAmount())
                .build();

//        Feed feed = new Feed(
//                memberId,
//                req.getContent(),
//                req.getChallengeId(), // 찾은 Challenge 객체의 title를 사용
//                req.getHashtag(),
//                req.getDepositAmount()
//        );
        // 피드 객체에 멤버 설정
//        feed.setMemberId(member.getId());
        //feed.setChallenge(challenge);

        // 입금액이 있는 경우 챌린지의 presentAmount 업데이트
//        if (req.getDepositAmount() != null) {
//            challenge.setPresentAmount(challenge.getPresentAmount() + req.getDepositAmount());
//            challengeRepository.save(challenge);
//        }

        log.info(feed.toString());
        // 피드 저장소에서 피드 저장 후 저장된 피드 객체 가져오기
        Feed savedFeed = feedRepository.save(feed);
        // 저장된 피드 객체를 사용하여 응답 데이터 전송
        return FeedCreateResponse.toDto(savedFeed);
    }



    // 피드 전체 조회
    @Transactional(readOnly = true)
    public List<FeedCreateResponse> getAllFeeds(Long memberId) {
        List<Feed> feeds = feedRepository.findAll();
        List<FeedCreateResponse> feedResponses = new ArrayList<>();
        // feeds 리스트의 각 Feed 객체를 FeedCreateResponse로 변환하여 feedResponses 리스트에 저장
        feeds.forEach(feed -> feedResponses.add(FeedCreateResponse.toDto(feed)));
        // 변환된 피드 목록을 반환
        return feedResponses;
    }

    // 특정 회원의 피드 전체 조회
    @Transactional(readOnly = true)
    public List<FeedCreateResponse> getAllFeedsForMember(Long memberId) {
        List<Feed> feeds = feedRepository.findAllByMemberId(memberId);
        return feeds.stream()
                .map(FeedCreateResponse::toDto)
                .collect(Collectors.toList());
    }


    // 피드 상세 조회
    @Transactional(readOnly = true)
    public FeedResponse findFeed(final Long id) {
        Optional<Feed> oFeed = feedRepository.findById(id);
        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));

        return FeedResponse.toDto(feed);
    }


    //throws AuthorizationException 삭제 했을 때.
    @Transactional
    public FeedCreateResponse updateFeed(Long id, FeedUpdateRequest req, Long memberId, String jwt) throws AuthorizationException {
        Optional<Feed> oFeed = feedRepository.findById(id);
        Optional<Member> oMember = memberRepository.findById(memberId);

        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));
        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("삭제 할 권한이 없습니다. id: " + memberId));

        if (member.getId().equals(feed.getMemberId())) {
            feed.setContent(req.getContent());
//            feed.setChallengeId(req.getChallengeId());
            Optional<Challenge> oChallenge = challengeRepository.findById(req.getChallengeId());
            Challenge challenge = oChallenge.orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다. id: " + req.getChallengeId()));
            feed.setChallenge(challenge);
            feed.setHashtag(req.getHashtag());
            feed.setDepositAmount(req.getDepositAmount());

            Feed updatedFeed = feedRepository.save(feed);
            return FeedCreateResponse.toDto(updatedFeed);
        } else {
            throw new AuthorizationException("권한이 없습니다.");
        }
    }

    @Transactional
    public void addLikeCount(Long feedId) {
        Optional<Feed> oFeed = feedRepository.findById(feedId);
        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + feedId));
        feed.setLikeCount(feed.getLikeCount() + 1);
        feedRepository.save(feed);
    }

    @Transactional
    public void subLikeCount(Long feedId) {
        Optional<Feed> oFeed = feedRepository.findById(feedId);
        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + feedId));

        feed.setLikeCount(Math.max(feed.getLikeCount() - 1, 0));
        feedRepository.save(feed);
    }

    @Override
    public Object createFeed(FeedCreateResponse response) {
        return null;
    }

    // 피드 삭제 메소드
    @Transactional
    public void deleteFeed(Long id, @RequestHeader("Authorization") String jwt) { // 불필요한 인자 제거
        // JWT 토큰에서 사용자 ID 추출
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        Optional<Feed> oFeed = feedRepository.findById(id);
        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));

        log.info(String.valueOf(feed.getMemberId()));

        if (!feed.getMemberId().equals(memberId)) { // 피드 작성자의 멤버 ID와 현재 멤버의 ID 비교
            throw new UnauthorizedException("게시글 삭제 권한이 없습니다.");
        }

        feedRepository.deleteById(id);
    }
}
