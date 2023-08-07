package com.d210.moneymoa.service;



import com.d210.moneymoa.dto.LikedDeposit;
import com.d210.moneymoa.dto.LikedSaving;
import com.d210.moneymoa.dto.Saving;

import java.util.List;

public interface SavingService {

    void saveSavingProducts() throws InterruptedException;

    List<Saving> getAllSavingProducts() throws InterruptedException;

    Saving getSavingProductWithInterestDetails(String productCode);

    void saveLikedSaving(LikedSaving likedSaving);
}
