package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.FeedFile;
import org.springframework.stereotype.Service;

@Service
public interface FeedFileService {
    void saveFeedFile(FeedFile feedFile);
}
