package com.d210.moneymoa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateInfo implements Serializable {

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "비밀번호")
    private String password;

    @ApiModelProperty(value = "한줄 소개")
    private String introduce;

    @ApiModelProperty(value = "프로필 이미지 경로")
    private String imageUrl;

    public MemberUpdateInfo(String nickname, String password, String introduce) {
        this.nickname = nickname;
        this.password = password;
        this.introduce = introduce;
    }
}
