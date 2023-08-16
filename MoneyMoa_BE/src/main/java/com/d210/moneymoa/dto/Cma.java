package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@NoArgsConstructor
public class Cma  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cmaName;

    private String stockName;

    private String maxRate;

    @Column(length = 3000)
    private String memo;

    @OneToMany(mappedBy = "cma", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LikedCma> likedCmas;

    @Builder
    public Cma(Long id, String cmaName, String stockName, String maxRate, String memo) {
        this.id = id;
        this.cmaName = cmaName;
        this.stockName = stockName;
        this.maxRate = maxRate;
        this.memo = memo;
    }

}
