package com.d210.moneymoa.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeResponse {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = LocalDateDeserializer.class)
    private String period; // 목표날짜

    private Integer goalAmount; // 목표금액

    @Builder
    public static ChallengeResponse toDto(Challenge challnege) {
        return new ChallengeResponse(
                challnege.getId(),
                challnege.getTitle(),
                challnege.getContent(),
                challnege.getPeriod(),
                challnege.getGoalAmount()
                );

    }
    }

