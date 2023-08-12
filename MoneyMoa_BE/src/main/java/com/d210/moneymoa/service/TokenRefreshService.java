package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.AuthToken;

public interface TokenRefreshService {
    String refreshAccessToken(AuthToken expiredAuthToken);
}
