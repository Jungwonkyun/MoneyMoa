package com.d210.moneymoa.dto.challenge;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {

    // id, 제목, 내용, 기간, 전체[목표금액], 작성자id, 챌린지 id, 시작일, presentAmount int[현재 유저가 모은 금액]

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    private Long memberId;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String period; // 기간

    private Integer goalAmount; // 목표금액


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate; // 날짜
    @PrePersist
    public void createDateTime() {
        this.startDate = LocalDate.now();
    }

    private Integer presentAmount; // 현재 유저가 모은 금액

    @Builder
    public Challenge(Long memberId, String title, String content, String period, Integer goalAmount) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.period = period;
        this.goalAmount = goalAmount;
    }


}
