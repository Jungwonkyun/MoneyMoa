package com.d210.moneymoa.dto.feed;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "게시글 수정")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedUpdateRequest {

    @ApiModelProperty(value = "게시글 내용", notes = "게시글 내용을 입력해주세요.")
    private String content;

    public String challenge;

    public String hashtag;

    public Integer depositAmount;

    public Long memberId;

}
