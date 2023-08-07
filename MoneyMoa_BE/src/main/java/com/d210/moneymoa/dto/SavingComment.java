package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class SavingComment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private String productCode;

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productCode", referencedColumnName = "productCode", insertable = false, updatable = false)
    private Saving saving; //code

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member; //id

    private String content;

    @CreationTimestamp
    @ApiModelProperty(hidden = true)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @ApiModelProperty(hidden = true)
    private String nickname;
}
