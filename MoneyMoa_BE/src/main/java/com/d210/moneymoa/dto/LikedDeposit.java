package com.d210.moneymoa.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikedDeposit implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(example = "0")
    private Long memberId;

    @ApiModelProperty(example = "string")
    private String productCode;

    @ApiModelProperty(example = "0")
    private Long amount;

    @ApiModelProperty(example = "0")
    private double interest;

    @ApiModelProperty(example = "0")
    private Integer period;

    @ApiModelProperty(example = "0")
    private Long result;

    @ApiModelProperty(example = "string")
    private String rsrvType;

    @Transient
    private String productName;

    @Transient
    private String bankName;

    @JsonGetter("productName")
    public String getProductName() {
        return this.getDeposit() != null ? this.getDeposit().getProductName() : "";
    }

    @JsonGetter("bankName")
    public String getBankName() {
        return this.getDeposit() != null ? this.getDeposit().getBankName() : "";
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member; //id

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productCode", referencedColumnName = "productCode", insertable = false, updatable = false)
    private Deposit deposit; //code

}
