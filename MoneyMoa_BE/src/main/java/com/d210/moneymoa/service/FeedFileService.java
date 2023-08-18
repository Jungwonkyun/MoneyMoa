package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.FeedFile;
import org.springframework.stereotype.Service;

public interface FeedFileService {
    void saveFeedFile(FeedFile feedFile);

    void deleteFeedFile(FeedFile feed); // 메서드 추가
}
