package com.d210.moneymoa.controller;

import com.d210.moneymoa.Exception.AuthorizationException;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.feed.FeedCreateRequest;
import com.d210.moneymoa.dto.feed.FeedCreateResponse;
import com.d210.moneymoa.dto.feed.FeedUpdateRequest;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.response.Response;
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
import java.util.List;
import java.util.Map;

@Api(value = "Feed Controller", tags = "Feed-Controller")
@RestController
@Slf4j
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;



    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "피드 생성", notes = "피드를 작성합니다.")
    @PostMapping("/create")
    public Response createFeed(@RequestBody  FeedCreateRequest req,
                               @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                               @RequestHeader("Authorization") String jwt){
        jwt = jwt.replace("Bearer ", "");
        Long Id = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.createFeed(req, Id, jwt));
    }


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
        List<FeedCreateResponse> feedResponses = feedService.getAllFeedsForMember(memberId);

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
    public Response updateFeed(@PathVariable Long id, @RequestBody FeedUpdateRequest req,
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


/*
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
