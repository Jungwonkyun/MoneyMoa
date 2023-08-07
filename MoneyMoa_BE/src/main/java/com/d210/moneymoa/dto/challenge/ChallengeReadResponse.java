package com.d210.moneymoa.dto.challenge;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ChallengeReadResponse {
    private Long id;
    private String title;
    private String content;
    private String period;
    private Integer goal_amount;
    private Long memberId;
    private LocalDate startDate;
    private Integer presentAmount;

    public ChallengeReadResponse(Challenge challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.period = challenge.getPeriod();
        this.goal_amount = challenge.getGoalAmount();
        this.memberId = challenge.getMemberId();
        this.startDate = challenge.getStartDate();
        this.presentAmount = challenge.getPresentAmount();
    }
}
