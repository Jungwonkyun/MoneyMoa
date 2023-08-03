package com.d210.moneymoa.dto.feed;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiOperation(value = "피드 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedCreateRequest {

    @ApiModelProperty(value = "피드 내용", notes = "피드 내용을 입력해주세요.", required = true, example = "피드 내용")
    private String content;

    private String challenge;

    private String hashtag;

    private Integer depositAmount;




}
