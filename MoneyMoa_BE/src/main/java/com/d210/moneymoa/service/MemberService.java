package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.dto.AuthToken;
import com.d210.moneymoa.dto.Member;

public interface MemberService {

    Member findMemberByEmail(String email);
    AuthTokens login(Member member);
    Long findOrCreateMember(Member member);
    Long newMember(Member member);
    Long setUserNickName(String UsernickName, String accessToken);
    void quitService(Long id);
    Member findLoginMember(String email, String password);
    void mailSend(String email, String pwd);

    Member updateMember(Member updatedMember);

    Member findMemberById(Long memberId);
    AuthToken logout(AuthToken accessToken);
}
