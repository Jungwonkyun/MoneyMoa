package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginInfo implements Serializable {
    private String email;
    private String password;

    public LoginInfo() {}
    public LoginInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}