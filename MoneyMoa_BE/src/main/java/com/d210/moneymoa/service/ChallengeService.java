package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.ChallengeAuthorizationException;
import com.d210.moneymoa.Exception.ChallengeNotFoundException;
import com.d210.moneymoa.dto.challenge.ChallengeCreateRequest;
import com.d210.moneymoa.dto.challenge.ChallengeCreateResponse;
import com.d210.moneymoa.dto.challenge.ChallengeResponse;
import com.d210.moneymoa.dto.challenge.ChallengeUpdateRequest;

import java.util.List;

public interface ChallengeService {
    ChallengeCreateResponse createChallenge(ChallengeCreateRequest req, Long memberId);

    List<ChallengeCreateResponse> getMemberChallenges(Long memberId);

    ChallengeResponse findChallenge (Long id);

    ChallengeCreateResponse updateChallenge(Long id, ChallengeUpdateRequest req, String jwt) throws ChallengeAuthorizationException;

    void deleteChallenge(Long id, String jwt) throws ChallengeNotFoundException;
    /*

     */
}
