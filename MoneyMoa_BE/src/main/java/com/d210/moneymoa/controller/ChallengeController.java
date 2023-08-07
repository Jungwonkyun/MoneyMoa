package com.d210.moneymoa.controller;

import com.d210.moneymoa.Exception.ChallengeAuthorizationException;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.challenge.ChallengeCreateRequest;
import com.d210.moneymoa.dto.challenge.ChallengeCreateResponse;
import com.d210.moneymoa.dto.challenge.ChallengeUpdateRequest;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.response.Response;
import com.d210.moneymoa.service.ChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Challenge Controller", tags = "Challenge-Controller")
@RestController
@Slf4j
@RequestMapping("api/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;


    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "챌린지 생성", notes = "챌린지 작성합니다.")
    @PostMapping("/create")
    public Response createChallenge(@ModelAttribute final ChallengeCreateRequest req, @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt){
        jwt = jwt.replace("Bearer ", "");
        Long Id = authTokensGenerator.extractMemberId(jwt);
        ChallengeCreateResponse challengeCreateResponse = challengeService.createChallenge(req, Id);
        return Response.success(challengeCreateResponse);
    }

    @ApiOperation(value = "첼린지 전체 조회", notes = "내 첼린지 전체 목록을 조회합니다")
    @GetMapping("/list/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response getAllMyChallenges(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(challengeService.getMyChallenges());
    }

    @ApiOperation(value = "챌린지 상세 조회", notes = "챌린지을 상세 조회합니다")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findFeed(@ApiParam(value = "챌린지 id", required = true) @PathVariable final Long id, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(challengeService.findChallenge(id));
    }


    @ApiOperation(value = "챌린지 수정", notes = "챌린지을 수정합니다.")
    @GetMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateChallenge(@PathVariable Long id, @ModelAttribute ChallengeUpdateRequest req, @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) throws ChallengeAuthorizationException {
        jwt = jwt.replace("Bearer ", "");

        return Response.success(challengeService.updateChallenge(id, req, jwt));
    }

    @ApiOperation(value = "챌린지 삭제", notes = "챌린지을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteChallenge(@ApiParam(value = "챌린지 id", required = true) @PathVariable Long id, @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        challengeService.deleteChallenge(id, jwt);
        return Response.success("게시물이 삭제되었습니다.");
    }

    /*
    */
}

