package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthTokensRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByAccessToken(String accessToken);
}
