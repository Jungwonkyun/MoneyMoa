package com.d210.moneymoa.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Deposit implements Serializable {

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

    @OneToMany(mappedBy = "deposit", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonProperty("interestDetails")
    private List<DepositInterestDetail> depositInterestDetails;

    @OneToMany(mappedBy = "deposit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<LikedDeposit> likedDeposits;

    @OneToMany(mappedBy = "deposit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DepositComment> depositComments;

    @Builder
    public Deposit(String productCode, String bankCode, String bankName, String productName, String interest, String spcl, String joinMember, String etcNote, Long maxLimit) {
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