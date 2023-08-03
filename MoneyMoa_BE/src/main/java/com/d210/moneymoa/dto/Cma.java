package com.d210.moneymoa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


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

    @Builder
    public Cma(Long id, String cmaName, String stockName, String maxRate, String memo) {
        this.id = id;
        this.cmaName = cmaName;
        this.stockName = stockName;
        this.maxRate = maxRate;
        this.memo = memo;
    }

}
