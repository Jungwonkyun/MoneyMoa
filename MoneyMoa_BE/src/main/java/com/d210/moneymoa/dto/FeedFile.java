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
public class FeedFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedNo", referencedColumnName = "id")
    private Feed feed;

    @Builder
    public FeedFile(String imgPath, Feed feed) {
        this.imgPath = imgPath;
        this.feed = feed;
    }

    @Override
    public String toString() {
        return "FeedFile {" +
                "id=" + id +
                ", imgPath='" + imgPath + '\'' +
                ", feedId=" + (feed != null ? feed.getId() : "null") +
                '}';
    }
}
