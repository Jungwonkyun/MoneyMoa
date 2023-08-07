package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.AuthorizationException;
import com.d210.moneymoa.Exception.FeedNotFoundException;
import com.d210.moneymoa.Exception.MemberNotFoundException;
import com.d210.moneymoa.Exception.UnauthorizedException;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.feed.*;
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

@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    AuthTokensGenerator authTokensGenerator;



    @Transactional
    public FeedCreateResponse createFeed(FeedCreateRequest req, Long memberId) {
        // 멤버 저장소에서 memberId를 사용하여 멤버 찾기
        Optional<Member> oMember = memberRepository.findById(memberId);
        // 멤버가 없으면 MemberNotFoundException 발생시키기
        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId));
        log.info(member.toString());
//        String nickname = member.getNickname();

        // 요청 데이터를 사용하여 새로운 피드 객체 생성
        Feed feed = new Feed(
                req.getMemberId(),
                req.getContent(),
                req.getChallenge(),
                req.getHashtag(),
                req.getDepositAmount()
        );
        // 피드 객체에 멤버 설정
        feed.setMemberId(member.getId());
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

    // 피드 상세 조회
    @Transactional(readOnly = true)
    public FeedResponse findFeed(final Long id) {
        Optional<Feed> oFeed = feedRepository.findById(id);
        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));

        return FeedResponse.toDto(feed);
    }
//        return FeedResponse.toDto(feed, feed.getMember() != null ? feed.getMember().getNickname() : "");


//throws AuthorizationException 삭제 했을 때.
    @Transactional
    public FeedCreateResponse updateFeed(Long id, FeedUpdateRequest req, Long memberId, String jwt) throws AuthorizationException {
        Optional<Feed> oFeed = feedRepository.findById(id);
        Optional<Member> oMember = memberRepository.findById(memberId);

        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));
        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("삭제 할 권한이 없습니다. id: " + memberId));

        if (member.getId().equals(feed.getMemberId())) {
            feed.setContent(req.getContent());
            feed.setChallenge(req.getChallenge());
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
