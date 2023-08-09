package com.d210.moneymoa.dto.feed;

import com.d210.moneymoa.dto.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member; //id


}