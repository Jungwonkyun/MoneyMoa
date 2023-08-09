package com.d210.moneymoa.controller;


import com.d210.moneymoa.dto.FeedLikeDto.FeedLikeRequest;
import com.d210.moneymoa.response.Response;
import com.d210.moneymoa.service.FeedLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.d210.moneymoa.response.Response.success;

@Api(value = "FeedLike Controller", tags = "FeedLike-Controller")
@RestController
@Slf4j
@RequestMapping("api/feed/like")
public class FeedLikeController {
    @Autowired
    private FeedLikeService feedLikeService;

    @ApiOperation(value = "피드 좋아요", notes = "피드 좋아요를 합니다.")
    @PostMapping("/insert")
    public Response insert(@RequestBody FeedLikeRequest feedLikeRequest) throws Exception {
        feedLikeService.insert(feedLikeRequest);
        return success();
    }

    @ApiOperation(value = "피드 좋아요 취소", notes = "피드 좋아요를 취소합니다.")
    @PostMapping("/delete")
    public Response delete(@RequestBody FeedLikeRequest feedLikeRequest) {
        feedLikeService.delete(feedLikeRequest);
        return success();
    }
}
