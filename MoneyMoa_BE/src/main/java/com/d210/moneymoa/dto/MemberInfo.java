package com.d210.moneymoa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class MemberInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ApiModelProperty(value = "자기소개")
    private String introduce;

    @ApiModelProperty(value = "챌린지진척도")
    private Integer progress;

    @ApiModelProperty(value = "프로필사진")
    private String originName;

    @ApiModelProperty(value = "프로필사진 S3 저장이름")
    private String saveName;

    public MemberInfo(Member member, String introduce, Integer progress, String originName, String saveName) {
        this.member = member;
        this.introduce = introduce;
        this.progress = progress;
        this.originName = originName;
        this.saveName = saveName;
    }
}
