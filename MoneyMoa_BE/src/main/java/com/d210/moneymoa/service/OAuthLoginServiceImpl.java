package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.*;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.Role;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthLoginServiceImpl implements OAuthLoginService {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        return authTokensGenerator.generate(memberId);
    }

    public Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    @Transactional
    public Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .name(oAuthInfoResponse.getNickname())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .password("SNS LOGIN")
                .nickname(oAuthInfoResponse.getNickname())
                .role(Role.MEMBER)
                .build();

        return memberRepository.save(member).getId();
    }

    public Member findLogin(AuthTokens authTokens){

        Long id = authTokensGenerator.extractMemberId(authTokens.toString());
        Optional<Member> oMember = memberRepository.findById(id);

        return oMember.orElseGet(null);
    }

}
