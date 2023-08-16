package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.FeedFile;
import com.d210.moneymoa.repository.FeedFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    @Transactional
    public void deleteFeedFile(FeedFile feedfile) {
        log.info("deleteFeedFile 메서드 실행");
        Optional<FeedFile> OptFeedFile = feedFileRepository.findById(feedfile.getId());

        if (OptFeedFile.isPresent()) {
            FeedFile existingFeedFile = OptFeedFile.get();

            List<FeedFile> filesToRemove = new ArrayList<>();

            // Identify which FeedFile references to remove
            if(existingFeedFile.getFeed() != null && existingFeedFile.getFeed().getFeedFiles() != null) {
                for (FeedFile file : existingFeedFile.getFeed().getFeedFiles()) {
                    if (file.getId().equals(existingFeedFile.getId())) {
                        filesToRemove.add(file);
                    }
                }
            }

            // Now, safely remove the FeedFile references
            existingFeedFile.getFeed().getFeedFiles().removeAll(filesToRemove);

            log.info("Deleting FeedFile with ID: " + feedfile.getId());
            feedFileRepository.deleteById(feedfile.getId());
            log.info("FeedFile with ID: " + feedfile.getId() + " has been deleted.");
        }
    }
}
