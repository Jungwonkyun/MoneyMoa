package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.ChallengeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeFileRepository extends JpaRepository<ChallengeFile, Long> {
    Optional<ChallengeFile> findById(Long id);
}
