package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
