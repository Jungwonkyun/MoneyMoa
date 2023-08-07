package com.d210.moneymoa.service;


import com.d210.moneymoa.dto.Cma;
import com.d210.moneymoa.dto.LikedCma;
import com.d210.moneymoa.dto.LikedDeposit;

import java.util.List;
import java.util.Map;

public interface CmaService {
    void saveCmaProducts() throws InterruptedException;

    List<Cma> getCmaProducts() throws InterruptedException;

    List<Map<String, Object>> getCmaProductsAsMap() throws InterruptedException;

    Map<String, Object> getCmaProductByIdAsMap(Long productId) throws InterruptedException;

    void saveLikedCma(LikedCma likedCma);

}
