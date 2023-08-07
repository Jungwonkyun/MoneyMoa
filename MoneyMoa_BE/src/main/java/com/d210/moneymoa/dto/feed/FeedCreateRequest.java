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

    Long memberId;

    @ApiModelProperty(value = "피드 내용", notes = "피드 내용을 입력해주세요.", required = true, example = "피드 내용")
    String content;

    @ApiModelProperty(value = "챌린지 선택", notes = "챌린지를 선택해주세요.", required = true, example = "챌린지")
    String challenge;

    String hashtag;

    @ApiModelProperty(value = "저축 금액", notes = "저축 금액을 입력해주세요.", required = true, example = "저축 금액")
    Integer depositAmount;





}
