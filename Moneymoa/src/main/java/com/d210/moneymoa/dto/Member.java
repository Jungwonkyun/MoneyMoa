package com.d210.moneymoa.dto;
import javax.persistence.*;

import com.d210.moneymoa.domain.oauth.OAuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;


@Setter
@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String role;

    private String nickname;

    private String usernickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    private int valid;

    @Builder
    public Member(String email, String nickname, String role, OAuthProvider oAuthProvider, String password, int valid) {
        this.email = email;
        this.nickname = nickname;
        this.oAuthProvider = oAuthProvider;
        this.role = role;
        this.password = password;
        this.valid = 1;
    }

    @Override
    public String toString(){
        return this.email+" "+this.nickname+" "+this.usernickname+" "+this.oAuthProvider;
    }
}
