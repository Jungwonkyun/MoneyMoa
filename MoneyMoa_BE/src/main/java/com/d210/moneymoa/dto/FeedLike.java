package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class FeedLike {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(hidden = true)
    private String nickname;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member;

    @ApiModelProperty(hidden = true)
    private Long memberId;

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long feedId;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId", insertable = false, updatable = false)
    private Feed feed;



    @Builder
    public FeedLike(Feed feed, Member member) {
        this.feed = feed;
        this.feedId = feed.getId();
        this.member = member;
        this.memberId = member.getId();
        this.nickname = member.getNickname();
    }

}
