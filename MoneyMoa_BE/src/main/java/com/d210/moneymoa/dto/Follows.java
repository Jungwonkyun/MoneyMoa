package com.d210.moneymoa.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Follows implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromMemberId;
    private String fromMemberNickname;
    private  Long toMemberId;
    private String toMemberNickname;

    @Builder
    public Follows(Long id, Long fromMemberId, String fromMemberNickname, Long toMemberId, String toMemberNickname) {
        this.id = id;
        this.fromMemberId = fromMemberId;
        this.fromMemberNickname = fromMemberNickname;
        this.toMemberId = toMemberId;
        this.toMemberNickname = toMemberNickname;
    }
}
