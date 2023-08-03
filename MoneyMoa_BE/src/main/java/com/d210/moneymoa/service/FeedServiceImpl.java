package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.FeedNotFoundException;
import com.d210.moneymoa.Exception.MemberNotFoundException;
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
        // nickname을 DTO로 부터 직접 가져옵니다.
//        String nickname = req.getNickname();
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseGet(null);
        if (member == null){
            new MemberNotFoundException("사용자를 찾을 수 없습니다.");
        }

        log.info(member.toString());

        String nickname = member.getNickname();

        if (member != null) {
            Feed feed = new Feed(
                    req.getContent(),
                    req.getChallenge(),
                    req.getHashtag(),
                    req.getDepositAmount()
                    );
            //feed.setMember(member);
            feed.setNickname(nickname);
            Feed savedFeed = feedRepository.save(feed);

            return FeedCreateResponse.toDto(savedFeed);
        } else {
            throw new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId);
        }
    }

    // 피드 전체 조회
    @Transactional(readOnly = true)
    public List<FeedCreateResponse> getAllFeeds() {
        List<Feed> feeds = feedRepository.findAll();
        List<FeedCreateResponse> feedResponses = new ArrayList<>();
        // feeds 리스트의 각 Feed 객체를 FeedCreateResponse DTO로 변환하여 feedResponses 리스트에 저장
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
//        return FeedResponse.toDto(feed, feed.getMember() != null ? feed.getMember().getNickname() : "");
    }


    // 피드 수정
    @Transactional
    public FeedCreateResponse editFeed(final Long id, final FeedUpdateRequest req,  @RequestHeader("Authorization") String jwt) {
        Optional<Feed> oFeed = feedRepository.findById(id);
        Feed feed = oFeed.orElseThrow(() -> new FeedNotFoundException("피드를 찾을 수 없습니다. id: " + id));
        // 피드 수정하는 부분
        feed.setContent(req.getContent());
        feed.setChallenge(req.getChallenge());
        feed.setHashtag(req.getHashtag());
        feed.setDepositAmount(req.getDepositAmount());

        // 수정된 피드를 db에 반영
        Feed updatedFeed = feedRepository.save(feed);
        return FeedCreateResponse.toDto(updatedFeed);
        // jwt 을 갖고 비교함
    }

}
