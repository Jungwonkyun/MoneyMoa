package com.d210.moneymoa.dto.challenge;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiOperation(value = "챌린지 생성 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeCreateRequest {

    Long memberId;

    @ApiModelProperty(value = "챌린지 제목", notes = "챌린지 제목을 입력해주세요.", required = true, example = "챌린지 제목")
    String title;

    @ApiModelProperty(value = "챌린지 내용", notes = "챌린지 내용을 입력해주세요.", required = true, example = "챌린지 내용")
    String content;

    @ApiModelProperty(value = "챌린지 기간", example = "2023-12-31")
    String period;

    @ApiModelProperty(value = "챌린지 목표 금액", notes = "챌린지 목표 금액을 입력해주세요.", required = true, example = "챌린지 목표 금액")
    Integer goalAmount;

//    @ManyToOne(fetch = FetchType.LAZY) // Lazy 로딩 사용 (필요 시에만 로딩)
//    @JoinColumn(name = "member_id") // Member 엔티티의 PK와 연결되는 컬럼명
//    private Member member;
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
//    private LocalDateTime startDate; // 날짜
//    @PrePersist
//    public void createDateTime() {
//        this.startDate = LocalDateTime.now();
//    }
//    private Integer presentAmount; // 현재 유저가 모은 금액






}
