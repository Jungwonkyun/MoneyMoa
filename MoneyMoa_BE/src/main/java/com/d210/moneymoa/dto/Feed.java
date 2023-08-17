package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Feed implements Serializable {

    // 작성자, 내용, 챌린지 선택, 해시태그, 납입 금액, 이미지
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content; // 내용

    private String hashtag; // 해쉬태그

    private Integer depositAmount; // 납입 금액


    //    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @CreationTimestamp
    @ApiModelProperty(hidden = true)
    private LocalDateTime createDateTime; // 날짜

    //    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member; //id


    //    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private String nickname;

    //    name = "challengeId",
//    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long challengeId; // 챌린지 종류

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeId", insertable = false, updatable = false)
    private Challenge challenge; //id

    //@OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FeedFile> feedFiles;

    // fileUrls 필드 추가
    @Transient
    private List<String> fileUrls;

    // Getter 및 Setter
    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FeedLike> feedLikes;

    @Transient
    @ApiModelProperty(hidden = true)
    private String challengeTitle;

    //전체 조회의 좋아요 수
    @Transient
    private int likesCount;

    public int getLikesCount() {
        return this.likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    @Builder
        public Feed(String content, Long challengeId, String hashtag,
                    Integer depositAmount, Long memberId, String nickname,
                    String challengeTitle) //Integer firstLikeCount,
        { //
            this.content = content;
            this.challengeId = challengeId;
            this.hashtag = hashtag;
            this.depositAmount = depositAmount;
            this.memberId = memberId;
//            this.firstLikeCount = firstLikeCount;
            this.nickname = nickname;
            this.challengeTitle = challengeTitle;
        }
}


