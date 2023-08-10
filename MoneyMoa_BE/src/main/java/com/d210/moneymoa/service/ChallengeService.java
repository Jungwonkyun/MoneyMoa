package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Challenge;

public interface ChallengeService {
    public Challenge createChallenge(Challenge challenge, Long memberId);

    /*
    Challenge createChallenge(Challenge req, Long memberId);

    List<Challenge> getMemberChallenges(Long memberId);

    Challenge findChallenge (Long id);

    void updatePresentAmount(Long memberId, String challengeTitle, Integer depositAmount);

    Challenge updateChallenge(Long id, Challenge req, String jwt) throws ChallengeAuthorizationException;

    void deleteChallenge(Long id, String jwt) throws ChallengeNotFoundException;

     */

}
