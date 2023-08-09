package com.d210.moneymoa.dto.challenge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "챌린지 수정")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeUpdateRequest {

    @ApiModelProperty(value = "챌린지 제목 수정", example = "챌린지 제목 수정")
    private String title;

    @ApiModelProperty(value = "챌린지 내용 수정", example = "챌린지 내용 수정")
    private String content;

    @ApiModelProperty(value = "챌린지 기간 수정", example = "챌린지 기간 수정")
    private String period;

    @ApiModelProperty(value = "챌린지 목표 금액 수정", example = "챌린지 목표 금액 수정")
    private Integer goalAmount;

}
