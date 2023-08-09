package com.d210.moneymoa.domain.oauth;


import com.d210.moneymoa.domain.JwtTokenProvider;
import com.d210.moneymoa.dto.AuthToken;
import com.d210.moneymoa.repository.AuthTokensRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60* 30;  // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일


    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthTokensRepository authTokensRepository;

    public AuthTokens generate(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String subject = memberId.toString();
        String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt);

        AuthToken authToken = AuthToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(BEARER_TYPE)
                .expiresIn(ACCESS_TOKEN_EXPIRE_TIME / 1000L)
                .build();

        authTokensRepository.save(authToken);


        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }



}
