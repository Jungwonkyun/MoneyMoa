package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
public class Challenge {

    // id, 제목, 내용, 기간, 전체[목표금액], 작성자id, 챌린지 id, 시작일, presentAmount int[현재 유저가 모은 금액]

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

//    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId" , insertable=false, updatable = false)
    private Member member; //id

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String period; // 기간

    private Integer goalAmount; // 목표금액


    @CreationTimestamp
    @ApiModelProperty(hidden = true)
    private LocalDate startDate; // 날짜

    @ApiModelProperty(hidden = true)
    private Integer presentAmount; // 현재 유저가 모은 금액

//    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private String nickname;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, fetch =FetchType.LAZY)
    @JsonIgnore
    private List<Feed> feeds;



    @Builder
    public Challenge(String title, String content,
                     String nickname, String period, LocalDate startDate, Integer goalAmount
                  ) {                           // Integer presentAmount,Long memberId,
        this.title = title;
        this.content = content;
//        this.memberId = memberId;
        this.nickname = nickname;
        this.period = period;
        this.startDate = startDate;
        this.goalAmount = goalAmount;
//        this.presentAmount = presentAmount;

    }


}
