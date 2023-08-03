package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Saving implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;

    private String bankCode;

    private String bankName;

    private String productName;

    private String interest;

    private String spcl;

    private String joinMember;

    private String etcNote;

    private Long maxLimit;

    @Transient
    private List<String> spclList;

    @OneToMany(mappedBy = "saving", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JsonIgnore // 이 줄을 주석 처리하거나 제거합니다.
    @JsonManagedReference
    private List<SavingInterestDetail> savingInterestDetails;


    @Builder
    public Saving(String productCode, String bankCode, String bankName, String productName, String interest, String spcl, String joinMember, String etcNote, Long maxLimit) {
        this.productCode = productCode;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.productName = productName;
        this.interest = interest;
        this.spcl = spcl;
        this.joinMember = joinMember;
        this.etcNote = etcNote;
        this.maxLimit = maxLimit;
    }

}