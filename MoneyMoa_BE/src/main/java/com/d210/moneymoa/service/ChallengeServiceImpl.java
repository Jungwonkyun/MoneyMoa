package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Challenge;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    public Challenge createChallenge(Challenge inputChallenge, Long memberId) {
        // 사용자 닉네임 조회
        String nickName = memberRepository.findById(memberId).get().getNickname();
        LocalDate startDate = inputChallenge.getStartDate()!=null?inputChallenge.getStartDate():LocalDate.now();
        Challenge challenge = Challenge.builder()
                .title(inputChallenge.getTitle())
                .content(inputChallenge.getContent())
                .period(inputChallenge.getPeriod())
                .goalAmount(inputChallenge.getGoalAmount())
                .startDate(startDate) // startDate 값 설정
                .presentAmount(inputChallenge.getPresentAmount())
                .memberId(memberId) // memberId 설정
                .nickname(nickName) // 사용자 닉네임 설정
                .build();

//        String nickName = memberRepository.findById(memberId).get().getNickname();
//        challenge.setId(memberId);
//        challenge.setNickname(nickName);

        challengeRepository.save(challenge);
        return challenge;
        }

    /*

        log.info(String.valueOf(memberId));

        if (member != null) {
            Challenge challenge = Challenge.builder()
//                    .presentAmount()
                    .title(req.getTitle())
                    .period(req.getPeriod())
                    .content(req.getContent())
                    .goalAmount(req.getGoalAmount())
                    .memberId(req.getMemberId()).build();
//                    new Challenge(
//                    req.getMemberId(),
//                    req.getTitle(),
//                    req.getContent(),
//                    req.getPeriod(),
//                    req.getGoalAmount(),
//                    req.getPresentAmount()
            Challenge savedChallenge = challengeRepository.save(challenge);
            return Challenge.toDto(savedChallenge);
        } else {
            throw new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId);
        }
    }

    @Transactional(readOnly = true)
    public List<Challenge> getMemberChallenges(Long memberId) {
        List<Challenge> challenges = challengeRepository.findByMemberId(memberId);
        List<Challenge> challengeResponses = new ArrayList<>();
        challenges.forEach(challenge -> Challenge.add(Challenge.toDto(challenge)));
        return Challenge;
    }

    @Transactional(readOnly = true)
    public Challenge findChallenge(final Long id) {
        Optional<Challenge> oChallenge = challengeRepository.findById(id);
        Challenge challenge = oChallenge.orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다. id: " + id));
        return Challenge.toDto(challenge);
    }


    @Transactional
    public Challenge updateChallenge(Long id, Challenge req, String jwt) throws ChallengeAuthorizationException {
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
            return Challenge.toDto(updatedChallenge);
        } else {
            throw new ChallengeAuthorizationException("권한이 없습니다.");
        }
    }

    public void updatePresentAmount(Long memberId, String challengeTitle, Integer depositAmount) {
        List<Challenge> challenges = challengeRepository.findByMemberIdAndTitle(memberId, challengeTitle);
        if (!challenges.isEmpty()) {
            Challenge challenge = challenges.get(0);
            challenge.setPresentAmount(challenge.getPresentAmount() + depositAmount);
            challengeRepository.save(challenge);
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

     */
}