package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Challenge;
import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.FeedService;
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
import java.util.Optional;

@Api(value = "Feed Controller", tags = "Feed-Controller")
@RestController
@Slf4j
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @Autowired
    private ChallengeRepository challengeRepository;


    // 피드 생성 메서드
    // Swagger API 문서에 Endpoint 정보 추가
    @Api(value = "Feed Controller", tags = "Feed-Controller")
@RestController
@Slf4j
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @Autowired
    private ChallengeRepository challengeRepository;


    // 피드 생성 메서드
    // Swagger API 문서에 Endpoint 정보 추가
    @ApiOperation(value = "피드 생성", notes = "피드 작성합니다.")
    // POST 방식으로 "/create" URL에 매핑
    @PostMapping("/{challengeId}/create/")
    // 메서드의 반환 타입은 ResponseEntity 객체이며, 요청 본문에서 Feed 데이터를 파싱하고 인증 헤더를 사용합니다.
    public ResponseEntity<Map<String, Object>> createFeed(
            @PathVariable Long challengeId,
            @RequestBody Feed feed,
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

            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);
            if (challengeOptional.isPresent()) {
                // 존재하면 새 피드를 생성하고 입력받은 memberId, challengeId로 Feed 객체를 만들어 반환
                Feed newFeed = feedService.createFeed(challengeId, memberId, feed);

                // 피드가 성공적으로 생성되면 HTTP 상태를 CREATE로 변경
                status = HttpStatus.CREATED;
                //resultMap에 생성된 새 피드와 성공 메시지 추가
                resultMap.put("feed", newFeed);
                resultMap.put("message", "success");
            } else {
                //챌린지가 존재하지 않을 경우 HTTP 상태를 NOT_FOUND로 변경
                status = HttpStatus.NOT_FOUND;
                resultMap.put("message", "Challenge not found");
            }
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

        //    @GetMapping("/all")
//    @ResponseStatus(value = HttpStatus.OK)
//    public Response getAllFeeds(@RequestHeader("Authorization") String jwt) {
//        jwt = jwt.replace("Bearer ", "");
//        return Response.success(feedService.getAllFeeds());
//    }
    @ApiOperation(value = "피드 전체 조회", notes = "피드 전체목록을 조회합니다")
    @GetMapping("/all")
    public Response getAllFeeds(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.getAllFeeds(memberId));
    }


    @ApiOperation(value = "특정 회원의 피드 전체 조회", notes = "특정 회원의 피드 전체목록을 조회합니다")
    @GetMapping("/all/{memberId}")
    public Response getAllFeedsForMember(@PathVariable Long memberId,
                                         @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        List<Feed> feedResponses = feedService.getAllFeedsForMember(memberId);

//        Long memberId = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedResponses);
    }



    @ApiOperation(value = "피드 상세 조회", notes = "피드르 상세 조회합니다")
    @GetMapping("/{id}")
    public Response findFeed(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id,
                             @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(feedService.findFeed(id));
    }


    @ApiOperation(value = "피드 수정", notes = "피드를 수정합니다.")
    @PutMapping("/update/{id}")
    public Response updateFeed(@PathVariable Long id, @RequestBody Feed req,
                               @RequestHeader("Authorization") String jwt) throws AuthorizationException {
        jwt = jwt.replace("Bearer ", "");
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.updateFeed(id, req, memberId, jwt));
    }

    @ApiOperation(value = "피드 삭제", notes = "피드를 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public Response deleteFeed(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id,
                               @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) throws AuthorizationException {
        jwt = jwt.replace("Bearer ", "");
        feedService.deleteFeed(id, jwt);
        return Response.success("게시물이 삭제되었습니다.");
    }


    //
    // Swagger API 문서에 표시되는 "예금상품 댓글 삭제" 연산에 대한 설명
    @ApiOperation(value = "예금상품 댓글 삭제")
// HTTP DELETE 메서드로 요청을 받는 엔드포인트로, 삭제할 댓글의 ID를 경로 변수로 사용
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            // 경로 변수인 commentId를 Long 타입으로 매핑
            @PathVariable Long commentId,
            // 승인 헤더에 JWT 토큰 정보를 포함하도록 하며, Swagger에서는 "Bearer ${jwt token} 형식으로 전송"을 표시
            @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
            @RequestHeader("Authorization") String jwt) {

        // 응답에 반환할 결과 맵과 HTTP 상태를 저장할 변수를 초기화
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            // 권한 헤더에서 "Bearer " 접두사를 제거하여 순수한 JWT 토큰만 추출
            jwt = jwt.replace("Bearer ", "");
            // JWT 토큰에서 회원 ID를 추출
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            // 댓글 삭제 서비스를 호출하여 댓글을 삭제
            depositCommentService.deleteDepositComment(commentId, memberId);

            // 삭제에 성공한 경우, HTTP 상태를 OK로 설정하고 결과 맵에 성공 메시지를 추가
            status = HttpStatus.OK;
            resultMap.put("message", "success");
            resultMap.put("message2", "삭제 되었습니다.");
        } catch (IllegalAccessException e) {
            // 권한이 없는(예: 작성자가 아닌) 사용자가 삭제를 시도한 경우, 예외를 처리
            e.printStackTrace();
            status = HttpStatus.FORBIDDEN;
            resultMap.put("message", "fail");
            resultMap.put("message2", "작성자가 아닙니다.");
        } catch (Exception e) {
            // 기타 예외가 발생한 경우 처리 (예: JWT 토큰 또는 commentId 문제)
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", "fail");
            resultMap.put("message2", "jwttoken이 잘못되었거나 commentId가 잘못되었습니다.");
        }

        // 최종적으로 결과 맵과 상태 코드를 함께 반환. ResponseEntity를 사용하여 Map 객체와 함께 해당 상태코드를 클라이언트에게 전송
        return new ResponseEntity<>(resultMap, status);
    }

*/




}
