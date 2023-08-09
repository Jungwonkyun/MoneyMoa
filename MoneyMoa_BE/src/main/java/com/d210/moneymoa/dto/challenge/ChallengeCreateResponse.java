package com.d210.moneymoa.dto.challenge;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ChallengeCreateResponse {

    private Long memberId;
    private Long id;
    private String title;
    private String content;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String period;
    private Integer goalAmount;
    private LocalDate startDate;
    private Integer presentAmount;

    public ChallengeCreateResponse(Long id, String title, String content,
                                   String period, Integer goalAmount, Long memberId,
                                   LocalDate startDate, Integer presentAmount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.period = period;
        this.goalAmount = goalAmount;
        this.memberId = memberId;
        this.startDate = startDate;
        this.presentAmount = presentAmount;
    }

    public static ChallengeCreateResponse toDto(Challenge challenge) {
        return new ChallengeCreateResponse(
        challenge.getId(),
        challenge.getTitle(),
        challenge.getContent(),
        challenge.getPeriod(),
        challenge.getGoalAmount(),
        challenge.getMemberId(),
        challenge.getStartDate(),
        challenge.getPresentAmount()
        );
    }


}
