package com.d210.moneymoa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Wiki {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "Wiki 제목", required = true, example = "샘플 제목")
    @Column(length = 512)
    private String title;

    @ApiModelProperty(value = "Wiki 본문 내용", required = true, example = "샘플 내용")
    @Column(length = 8192)
    private String content;

    public Wiki() {}

    @Builder
    public Wiki(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
