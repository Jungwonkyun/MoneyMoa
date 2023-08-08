package com.d210.moneymoa.controller;

import com.d210.moneymoa.Exception.AuthorizationException;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.feed.FeedCreateRequest;
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
import org.springframework.web.bind.annotation.*;

@Api(value = "Feed Controller", tags = "Feed-Controller")
@RestController
@Slf4j
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiOperation(value = "게시글 생성", notes = "게시글 작성합니다.")
    @PostMapping("/create")
    public Response createFeed(@RequestBody  FeedCreateRequest req,
                               @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                               @RequestHeader("Authorization") String jwt){
        jwt = jwt.replace("Bearer ", "");
        Long Id = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.createFeed(req, Id));
    }


//    @GetMapping("/all")
//    @ResponseStatus(value = HttpStatus.OK)
//    public Response getAllFeeds(@RequestHeader("Authorization") String jwt) {
//        jwt = jwt.replace("Bearer ", "");
//        return Response.success(feedService.getAllFeeds());
//    }
    @ApiOperation(value = "게시글 전체 조회", notes = "게시글 전체목록을 조회합니다")
    @GetMapping("/all")
    @ResponseStatus(value = HttpStatus.OK)
    public Response getAllFeeds(@RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.getAllFeeds(memberId));
    }


    @ApiOperation(value = "게시글 상세 조회", notes = "게시글을 상세 조회합니다")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findFeed(@ApiParam(value = "게시글 id", required = true) @PathVariable Long id,
                             @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(feedService.findFeed(id));
    }


    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateFeed(@PathVariable Long id, @RequestBody FeedUpdateRequest req,
                               @RequestHeader("Authorization") String jwt) throws AuthorizationException {
        jwt = jwt.replace("Bearer ", "");
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.updateFeed(id, req, memberId, jwt));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteFeed(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id,
                               @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) throws AuthorizationException {
        jwt = jwt.replace("Bearer ", "");
        feedService.deleteFeed(id, jwt);
        return Response.success("게시물이 삭제되었습니다.");
    }

}
