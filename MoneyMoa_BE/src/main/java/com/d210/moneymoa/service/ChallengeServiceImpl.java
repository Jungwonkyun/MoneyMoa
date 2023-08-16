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
import java.util.List;
import java.util.NoSuchElementException;

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
        // presentAmount 값이 null일 경우 0으로 초기화하여 사용
        Integer presentAmount = inputChallenge.getPresentAmount() != null ? inputChallenge.getPresentAmount() : 0;
        Challenge challenge = Challenge.builder()
                .title(inputChallenge.getTitle())
                .content(inputChallenge.getContent())
                .period(inputChallenge.getPeriod())
                .goalAmount(inputChallenge.getGoalAmount())
                .startDate(startDate) // startDate 값 설정
                .presentAmount(presentAmount) // 0으로 초기화
                .memberId(memberId) // memberId 설정
                .nickname(nickName) // 사용자 닉네임 설정
                .build();

//        String nickName = memberRepository.findById(memberId).get().getNickname();
//        challenge.setId(memberId);
//        challenge.setNickname(nickName);

        challengeRepository.save(challenge);
        return challenge;
        }

    @Override //멤버의 챌린지 검색
    public List<Challenge> getMemberChallenges(Long memberId) {
        return challengeRepository.findAllByMemberId(memberId);
    }

    @Override
    public Challenge getChallenge(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("챌린지를 찾을 수 없습니다."));
    }

    //챌린지 수정
    @Override
    public void updateChallenge(Long id, Challenge challengeInfo, Long memberId) throws IllegalAccessException {
        Challenge challengeToUpdate = challengeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("챌린지를 찾을 수 없습니다."));

        if (!challengeToUpdate.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("수정 권한이 없습니다.");
        }
        //챌린지 업데이트 정보
        challengeToUpdate.setTitle(challengeInfo.getTitle());
        challengeToUpdate.setContent(challengeInfo.getContent());
        challengeToUpdate.setGoalAmount(challengeInfo.getGoalAmount());
        challengeToUpdate.setPeriod(challengeInfo.getPeriod());

        //변경된 정보 저장
        challengeRepository.save(challengeToUpdate);
    }

    @Override
    public void deleteChallenge(Long id, Long memberId) throws IllegalAccessException {
        Challenge challengeToDelete = challengeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("챌린지를 찾을 수 없습니다."));

        if (!challengeToDelete.getMemberId().equals(memberId)) {
            throw new IllegalAccessException("삭제 권한이 없습니다.");
        }
        challengeRepository.deleteById(id);
    }

}