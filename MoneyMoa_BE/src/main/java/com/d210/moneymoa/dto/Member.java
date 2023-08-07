package com.d210.moneymoa.dto;
import javax.persistence.*;

import com.d210.moneymoa.domain.oauth.OAuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@NoArgsConstructor
public class Member implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "이메일 주소")
    private String email;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "권한")
    private Role role;

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "비밀번호")
    private String password;

    @ApiModelProperty(value = "성별")
    private String gender;

    @ApiModelProperty(value = "생일")
    private String birthday;


    @ApiModelProperty(value = "로그인 경로")
    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @ApiModelProperty(value = "탈퇴한 회원인지 확인")
    private int valid;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LikedDeposit> likedDeposits;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DepositComment> depositComments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LikedSaving> likedSavings;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SavingComment> savingComments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LikedCma> likedCmas;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CmaComment> cmaComments;

    //OAuth로그인 버전
    @Builder
    public Member(String email, String name, Role role, OAuthProvider oAuthProvider, String password, String nickname) {
        this.email = email;
        this.name = name;
        this.oAuthProvider = oAuthProvider;
        this.role = role;
        this.password = password;
        this.nickname = nickname;
        this.valid = 1;
    }

    @Override
    public String toString(){
        return this.email+" "+this.name+" "+this.nickname+" "+this.oAuthProvider;
    }

}
