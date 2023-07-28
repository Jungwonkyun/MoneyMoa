package com.d210.moneymoa.domain.general;

import com.d210.moneymoa.domain.oauth.OAuthLoginParams;
import com.d210.moneymoa.domain.oauth.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class GeneralLoginParams implements OAuthLoginParams {
    private String authorizationCode;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GENERAL;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        return null;
    }

}
