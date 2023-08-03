package com.d210.moneymoa.dto.feed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class FeedImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false)
    private String originName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Feed feed;


    // 이미지 파일. 파일을 하나의 DTO 로 모아서 처리하고 그 디티오 폴더네임, 패스네임.
    // 폴더는 따로 관리하되, img url만 저장하는 걸로
//    private String imgUrl;



}
