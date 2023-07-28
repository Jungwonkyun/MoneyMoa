package com.d210.moneymoa.service;

import com.d210.moneymoa.domain.oauth.*;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AuthTokensGenerator authTokensGenerator;
    @Autowired
    JavaMailSender javaMailSender;


    //로그인시 유저이메일을 가져와서 DB에 회원정보가 있는지 확인
    public Member findMemberByEmail(String email) {
        Optional<Member> oMember = memberRepository.findByEmail(email);
        return oMember.orElse(null);
    }

    public AuthTokens login(Member member) {
        Long memberId = findOrCreateMember(member);
        return authTokensGenerator.generate(memberId);
    }

    public Long findOrCreateMember(Member member) {
        return memberRepository.findByEmail(member.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(member));
    }

    @Transactional
    public Long newMember(Member member) {
        Member newMember = Member.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .oAuthProvider(OAuthProvider.GENERAL)
                .password(member.getPassword())
                .role(member.getRole())
                .valid(1)
                .build();

        return memberRepository.save(member).getId();
    }


    //유저가 원하는 닉네임 설정
    @Transactional
    public Long setUserNickName(String UsernickName, String accessToken) {

        Long id = authTokensGenerator.extractMemberId(accessToken);
        Optional<Member> oMember = memberRepository.findById(id);

        if(oMember.isEmpty()) return 0L;

        Member member = oMember.get();
        member.setUsernickname(UsernickName);

        return  memberRepository.save(member).getId();
    }

    //회원탈퇴 => 논리적 삭제
    @Transactional
    public void quitService(Long id){
        Optional<Member> oMember = memberRepository.findById(id);

        if(oMember.isEmpty()) return;

        Member member = oMember.get();
        member.setValid(0);
    }


    public Member findLoginMember(String email, String password) {
        Optional<Member> oMember = memberRepository.findByEmailAndPassword(email,password);
        return oMember.orElse(null);
    }


    public void mailSend(String email, String pwd) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(email+"님의 비밀번호 확인 메일입니다");
        simpleMailMessage.setText(email+"님의 비밀번호는 "+pwd+"입니다");

        javaMailSender.send(simpleMailMessage);
    }


}
