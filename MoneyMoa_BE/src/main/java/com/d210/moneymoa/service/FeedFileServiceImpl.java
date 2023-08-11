package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.FeedFile;
import com.d210.moneymoa.repository.FeedFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeedFileServiceImpl implements FeedFileService {

    @Autowired
    FeedFileRepository feedFileRepository;

    // FeedFile 저장 로직 구현
    @Override
    public void saveFeedFile(FeedFile feedFile) {
        feedFileRepository.save(feedFile);
    }
}
