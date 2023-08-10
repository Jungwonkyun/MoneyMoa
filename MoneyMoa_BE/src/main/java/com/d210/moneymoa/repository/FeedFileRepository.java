package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.FeedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedFileRepository extends JpaRepository<FeedFile, Long> {
}