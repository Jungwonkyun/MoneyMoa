package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.domain.oauth.OAuthInfoResponse;
import com.d210.moneymoa.domain.oauth.OAuthLoginParams;

public interface OAuthLoginService {

    AuthTokens login(OAuthLoginParams params);
    Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse);
    Long newMember(OAuthInfoResponse oAuthInfoResponse);

}
