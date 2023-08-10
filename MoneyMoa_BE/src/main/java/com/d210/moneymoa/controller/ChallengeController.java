package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Challenge;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.FeedRepository;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.ChallengeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired

    private FeedRepository feedRepository;


    // 챌린지 생성 메서드
    // Swagger API 문서에 Endpoint 정보 추가
    @ApiOperation(value = "챌린지 생성", notes = "챌린지 작성합니다.")
    // POST 방식으로 "/create" URL에 매핑
    @PostMapping("/create")
    // 메서드의 반환 타입은 ResponseEntity 객체이며, 요청 본문에서 Challenge 데이터를 파싱하고 인증 헤더를 사용합니다.
    public ResponseEntity<Map<String, Object>> createChallenge(
            @RequestBody Challenge challenge,
            // JWT 토큰을 헤더에서 인증 정보로 사용
            @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
            @RequestHeader("Authorization") String jwt) {
        // 결과를 반환할 Map 객체 생성
        Map<String, Object> resultMap = new HashMap<>();
        // HTTP 상태 기본값 설정
        HttpStatus status;

        try {
            // "Bearer " 문자열을 제거하여 JWT 토큰 문자열만 얻음
            jwt = jwt.replace("Bearer ", "");
            // JWT 토큰에서 회원 ID를 추출
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            // 새 챌린지를 생성하고 입력받은 memberId로 Challenge 객체를 만들어 반환
            Challenge newChallenge = challengeService.createChallenge(challenge, memberId);

            // 챌린지가 성공적으로 생성되면 HTTP 상태를 CREATED로 변경
            status = HttpStatus.CREATED;
            // resultMap에 생성된 새 챌린지와 성공 메시지를 추가
            resultMap.put("challenge", newChallenge);
            resultMap.put("message", "success");

        } catch (Exception e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지를 추가
            resultMap.put("message", "fail");
        }

        // 최종 결과와 설정된 HTTP 상태를 반환하는 ResponseEntity 객체를 반환
        return new ResponseEntity<>(resultMap, status);
    }
    /*

    @ApiOperation(value = "{id}의 첼린지 목록 조회", notes = "{id}의 첼린지 전체 목록을 조회합니다")
    @GetMapping("/list/{memberId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Response getAllMemberChallenges(@PathVariable(name = "memberId") Long memberId,
                                           @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(challengeService.getMemberChallenges(memberId));
    }

    @ApiOperation(value = "챌린지 상세 조회", notes = "챌린지을 상세 조회합니다")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findFeed(@ApiParam(value = "챌린지 id", required = true) @PathVariable final Long id,
                             @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(challengeService.findChallenge(id));
    }


    @ApiOperation(value = "챌린지 수정", notes = "챌린지을 수정합니다.")
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateChallenge(@PathVariable Long id, @RequestBody Challenge req,
                                    @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) throws ChallengeAuthorizationException {
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
}

