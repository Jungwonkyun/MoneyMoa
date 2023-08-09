package com.d210.moneymoa.dto.feed;

import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.challenge.Challenge;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@ApiOperation(value = "피드 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedCreateRequest {


    @ApiModelProperty(value = "피드 내용", notes = "피드 내용을 입력해주세요.", required = true, example = "String")
    String content;

    //    @ApiModelProperty(hidden = true)
    @ApiModelProperty(value = "챌린지 선택", notes = "챌린지를 선택해주세요.", required = true, example = "String")
    @Column(nullable = false)
    private Long challengeId; // 챌린지 종류

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeId", referencedColumnName = "id", insertable = false, updatable = false)
    private Challenge challenge; //id

    @ApiModelProperty(value = "해시태그 입력", notes = "해시태그를 입력해주세요.", required = true, example = "String")
    String hashtag;

    @ApiModelProperty(value = "저축 금액", notes = "저축 금액을 입력해주세요.", required = true, example = "Integer")
    int depositAmount;

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member; //id





}
