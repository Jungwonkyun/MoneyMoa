package com.d210.moneymoa.controller;

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

@Api(value = "Feed Controller", tags = "Feed")
@RestController
@Slf4j
@RequestMapping("/feed")
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
    public Response createFeed(@ModelAttribute final FeedCreateRequest req, @RequestHeader("Authorization") String jwt){
        jwt = jwt.replace("Bearer ", "");
        Long Id = authTokensGenerator.extractMemberId(jwt);
        return Response.success(feedService.createFeed(req, Id));
    }


    @ApiOperation(value = "게시글 전체 조회", notes = "게시글 전체목록을 조회합니다")
    @GetMapping("/all")
    @ResponseStatus(value = HttpStatus.OK)
    public Response getAllFeeds() {
        return Response.success(feedService.getAllFeeds());
    }

    @ApiOperation(value = "게시글 상세 조회", notes = "게시글을 상세 조회합니다")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findFeed(@ApiParam(value = "게시글 id", required = true) @PathVariable final Long id) {
        return Response.success(feedService.findFeed(id));
    }


    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정합니다.")
    @GetMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editFeed(@PathVariable Long id, @ModelAttribute FeedUpdateRequest req, @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");

        return Response.success(feedService.editFeed(id, req, jwt));
    }
}
