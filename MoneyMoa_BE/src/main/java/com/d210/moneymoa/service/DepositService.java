package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Deposit;
import com.d210.moneymoa.dto.DepositFile;
import com.d210.moneymoa.dto.LikedDeposit;

import java.util.List;

public interface DepositService {

    void saveDepositProducts() throws InterruptedException;

    List<Deposit> getAllDepositProducts() throws InterruptedException;

    Deposit getDepositProductWithInterestDetails(String productCode);

    void saveLikedDeposit(LikedDeposit likedDeposit);

    Deposit findByBankCode(String bankCode);

    void saveDepositFile(DepositFile depositFile);

    List<LikedDeposit> myLikedDepositList(Long memberId);

    void deleteLikedDeposit(Long memberId, Long likeDepositId);
}
