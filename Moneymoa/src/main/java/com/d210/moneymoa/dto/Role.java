package com.d210.moneymoa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("ROLE_ADMIN,ROLE_MEMBER"),
    MEMBER("ROLE_MEMBER");

    private String value;
}