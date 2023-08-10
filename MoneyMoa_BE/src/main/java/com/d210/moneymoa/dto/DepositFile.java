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
public class DepositFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankCode", referencedColumnName = "bankCode")
    private Deposit deposit;

    @Builder
    public DepositFile(String imgPath, Deposit deposit) {
        this.imgPath = imgPath;
        this.deposit = deposit;
    }
}
