package com.d210.moneymoa.dto;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class SavingInterestDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;

    private Integer period;

    private String interestType;

    private String rsrvType;

    private String basicRate;

    private String maxRate;


    @ManyToOne
    @JoinColumn(name="productCode", referencedColumnName="productCode", insertable = false, updatable = false)
    @JsonIgnore
    private Saving saving;

    // Saving에 대한 Getter와 Setter를 추가합니다.
    public Saving getSaving() {
        return saving;
    }

    @Builder
    public SavingInterestDetail(String productCode, Integer period, String interestType, String rsrvType, String basicRate, String maxRate) {
        this.productCode = productCode;
        this.period = period;
        this.interestType = interestType;
        this.rsrvType = rsrvType;
        this.basicRate = basicRate;
        this.maxRate = maxRate;
    }
}
