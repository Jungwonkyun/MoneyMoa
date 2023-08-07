package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.LikedSaving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedSavingRepository extends JpaRepository<LikedSaving, Long> {
}

