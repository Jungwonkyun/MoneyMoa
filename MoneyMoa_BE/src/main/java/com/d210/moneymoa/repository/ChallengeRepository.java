package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

//    List<Challenge> findByMemberId(Long memberId);
//
//    List<Challenge> findByMemberIdAndTitle(Long memberId, String title);

    List<Challenge> findAllByMemberId(Long memberId);

//    Challenge getChallenge(Long challengeId);

//    void updateChallenge(Long id, Challenge challenge);

//    Challenge getChallengeName(Long challengeId);
}
