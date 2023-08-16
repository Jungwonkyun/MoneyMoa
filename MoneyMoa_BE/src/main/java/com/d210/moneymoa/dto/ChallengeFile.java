package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChallengeFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeNo", referencedColumnName = "id")
    private Challenge challenge;

    @Builder
    public ChallengeFile(String imgPath, Challenge challenge) {
        this.imgPath = imgPath;
        this.challenge = challenge;
    }

    @Override
    public String toString() {
        return "ChallengeFile {" +
                "id=" + id +
                ", imgPath='" + imgPath + '\'' +
                ", challengeId=" + (challenge != null ? challenge.getId() : "null") +
                '}';
    }
}
