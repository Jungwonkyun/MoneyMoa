package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.ChallengeAuthorizationException;
import com.d210.moneymoa.Exception.ChallengeNotFoundException;
import com.d210.moneymoa.Exception.MemberNotFoundException;
import com.d210.moneymoa.Exception.UnauthorizedException;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.challenge.*;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    AuthTokensGenerator authTokensGenerator;

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest req, Long memberId) {
        Optional<Member> oMember = memberRepository.findById(memberId);
        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId));
        if (member == null) {
            new MemberNotFoundException("사용자를 찾을 수 없습니다.");
        }

        log.info(member.toString());

        if (member != null) {
            Challenge challenge = new Challenge(
                    req.getMemberId(),
                    req.getTitle(),
                    req.getContent(),
                    req.getPeriod(),
                    req.getGoalAmount()
            );
            Challenge savedChallenge = challengeRepository.save(challenge);
            return ChallengeCreateResponse.toDto(savedChallenge);
        } else {
            throw new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId);
        }
    }

    @Transactional(readOnly = true)
    public List<ChallengeCreateResponse> getMyChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        List<ChallengeCreateResponse> challengeResponses = new ArrayList<>();
        challenges.forEach(challenge -> challengeResponses.add(ChallengeCreateResponse.toDto(challenge)));
        return challengeResponses;
    }

    @Transactional(readOnly = true)
    public ChallengeResponse findChallenge(final Long id) {
        Optional<Challenge> oChallenge = challengeRepository.findById(id);
        Challenge challenge = oChallenge.orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다. id: " + id));
        return ChallengeResponse.toDto(challenge);
    }


    @Transactional
    public ChallengeCreateResponse updateChallenge(Long id, ChallengeUpdateRequest req, String jwt) throws ChallengeAuthorizationException {
        Optional<Challenge> oChallenge = challengeRepository.findById(id);

        // JWT 토큰에서 사용자 ID 추출
        Long memberId = authTokensGenerator.extractMemberId(jwt);

        Optional<Member> oMember = memberRepository.findById(memberId);

        Challenge challenge = oChallenge.orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다. id: " + id));
        Member member = oMember.orElseThrow(() -> new MemberNotFoundException("수정할 권한이 없습니다. id: " + memberId));

        // 챌린지 작성자의 멤버 ID와 현재 멤버의 ID가 일치하는지 확인
        if (member.getId().equals(challenge.getMemberId())) {
            challenge.setTitle(req.getTitle());
            challenge.setContent(req.getContent());
            challenge.setPeriod(req.getPeriod());
            challenge.setGoalAmount(req.getGoalAmount());

            Challenge updatedChallenge = challengeRepository.save(challenge);
            return ChallengeCreateResponse.toDto(updatedChallenge);
        } else {
            throw new ChallengeAuthorizationException("권한이 없습니다.");
        }
    }


    @Transactional
    public void deleteChallenge(Long id, String jwt) throws ChallengeNotFoundException {
        // JWT 토큰에서 사용자 ID 추출
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        Optional<Challenge> oChallenge = challengeRepository.findById(id);
        Challenge challenge = oChallenge.orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다. id: " + id));

        log.info(String.valueOf(challenge.getMemberId()));
        if (!challenge.getMemberId().equals(memberId)) { // 피드 작성자의 멤버 ID와 현재 멤버의 ID 비교
            throw new UnauthorizedException("게시글 삭제 권한이 없습니다.");
        }

        challengeRepository.deleteById(id);
    }
}