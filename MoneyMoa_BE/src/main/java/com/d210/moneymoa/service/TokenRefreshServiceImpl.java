package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.JwtAuthenticationException;
import com.d210.moneymoa.domain.JwtTokenProvider;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.AuthToken;
import com.d210.moneymoa.dto.RefreshToken;
import com.d210.moneymoa.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TokenRefreshServiceImpl implements TokenRefreshService{

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    public String refreshAccessToken(AuthToken expiredAuthToken) {
        // 1. 엑세스 토큰에서 회원 ID 추출
        Long memberId = authTokensGenerator.extractMemberId(expiredAuthToken.getAccessToken());

        log.info(String.valueOf(memberId));

        // 2. Redis에서 RefreshToken 조회
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(memberId);

        if (refreshTokenOptional.isEmpty()) {
            throw new RuntimeException("Refresh token not found!");
        }

        try {
            // 3. RefreshToken 유효성 검증 (여기서는 간단하게 조회만 했지만, 실제로는 만료 시간 등 다양한 검증이 필요합니다)
            if (!jwtTokenProvider.validateToken(refreshTokenOptional.orElse(null).getRefreshToken())) {
                throw new RuntimeException("Refresh token is expired!");
            }
        }catch (JwtAuthenticationException e){
            throw new RuntimeException("Refresh token is expired!");
        }

        // 4. 새로운 엑세스 토큰 생성 및 반환
        return authTokensGenerator.generate(memberId).getAccessToken();
    }
}
