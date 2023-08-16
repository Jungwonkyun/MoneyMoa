package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.ChallengeFile;
import com.d210.moneymoa.repository.ChallengeFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ChallengeFileServiceImpl implements ChallengeFileService {

    @Autowired
    ChallengeFileRepository challengeFileRepository;

    // FeedFile 저장 로직 구현
    @Override
    public void saveChallengeFile(ChallengeFile challengeFile) {
        challengeFileRepository.save(challengeFile);
    }


}
