package com.d210.moneymoa.service;

import com.d210.moneymoa.Exception.EmailAlreadyRegisteredException;
import com.d210.moneymoa.Exception.InvalidLoginException;
import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.domain.oauth.OAuthProvider;
import com.d210.moneymoa.dto.AuthToken;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.Role;
import com.d210.moneymoa.repository.AuthTokensRepository;
import com.d210.moneymoa.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
// TODO: 2023-08-01  DB 비밀번호 암호화 필요
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AuthTokensGenerator authTokensGenerator;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthTokensRepository authTokensRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String authNum;


    //로그인시 유저이메일을 가져와서 DB에 회원정보가 있는지 확인
    public Member findMemberByEmail(String email) {
        Optional<Member> oMember = memberRepository.findByEmail(email);
        return oMember.orElse(null);
    }

    public AuthTokens login(Member member) {
        Long memberId = findOrCreateMember(member);
        return authTokensGenerator.generate(memberId);
    }

    public Long findMember(Member member) {
        Optional<Member> oMember = memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword());
        if(oMember.isEmpty()) return 0L;
        Member mem = oMember.get();
        return  mem.getId();
    }

    public Long findOrCreateMember(Member member) {
        return memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword())
                .map(Member::getId)
                .orElseGet(() -> newMember(member));
    }

    @Transactional
    public Long newMember(Member member) {
        Member newMember = Member.builder()
                .email(member.getEmail())
                .name(member.getName())
                .oAuthProvider(OAuthProvider.GENERAL)
//                .password(passwordEncoder.encode(member.getPassword()))
                .password(member.getPassword())
                .nickname(member.getNickname())
                .role(Role.MEMBER)
                .build();

        newMember.setBirthday(member.getBirthday());
        newMember.setGender(member.getGender());

        return memberRepository.save(newMember).getId();
    }


    //유저가 원하는 닉네임 설정
    @Transactional
    public Long setUserNickName(String nickname, String accessToken) {

        Long id = authTokensGenerator.extractMemberId(accessToken);
        Optional<Member> oMember = memberRepository.findById(id);

        if(oMember.isEmpty()) return 0L;

        Member member = oMember.get();
        member.setNickname(nickname);

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


    // 인증번호 8자리 무작위 생성
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();
        String authNum = "";
        for(int i=0; i<8; i++) {
            // 0~2 사이의 값을 랜덤하게 받아와 idx에 집어넣습니다.
            int idx = random.nextInt(3);

            // 랜덤하게 idx를 받았으면, 그 값을 switchcase를 통해 또 꼬아버립니다.
            // 숫자와 ASCII 코드를 이용합니다.
            switch (idx) {
                case 0 :
                    // a(97) ~ z(122)
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    // A(65) ~ Z(90)
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    // 0 ~ 9
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
        return authNum;
    }


    public MimeMessage createEmailForm(String email) throws UnsupportedEncodingException, MessagingException {
        // 코드를 생성합니다.
        authNum = createCode();
        String setFrom = "wjddnjsrbs97@gmail.com";	// 보내는 사람
        String toEmail = email;		// 받는 사람(값 받아옵니다.)
        String title = "머니모아 이메일 인증입니다";		// 메일 제목

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
        message.setSubject(title);		// 제목 설정

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 MoneyMoa 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authNum + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);		// 보내는 사람 설정
        // 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }


    public MimeMessage createEmailForm2(String email) throws MessagingException, UnsupportedEncodingException {
        // 코드를 생성합니다.
        authNum = createCode();
        String setFrom = "wjddnjsrbs97@gmail.com";	// 보내는 사람
        String toEmail = email;		// 받는 사람(값 받아옵니다.)
        String title = "머니모아 임시 비밀번호입니다";		// 메일 제목

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, toEmail);	// 받는 사람 설정
        message.setSubject(title);		// 제목 설정

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 MoneyMoa 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 비밀번호를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>로그인 후에 비밀번호 변경해주세요 감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>새로운 임시 비밀번호입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authNum + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);		// 보내는 사람 설정
        // 위 String으로 받은 내용을 아래에 넣어 내용을 설정합니다.
        message.setText(msgOfEmail, "utf-8", "html");

        return message;
    }


    public String sendEmail(String email) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);
        //실제 메일 전송
        javaMailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }

    public String sendEmail2(String email) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm2(email);
        //실제 메일 전송
        javaMailSender.send(emailForm);

        return authNum; //인증 코드 반환
    }

    public Long logout(String accessToken) {
        AuthToken AT = authTokensRepository.findByAccessToken(accessToken).get();
        AT.setExpiresIn(0L);

        authTokensRepository.save(AT);
        return AT.getExpiresIn();
    }


    @Transactional
    public Member updateMember(Member updatedMember) {
        Optional<Member> optionalMember = memberRepository.findById(updatedMember.getId());

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();

            // 필요한 속성들을 수정합니다 (예: email, name, nickname 등)
            existingMember.setEmail(updatedMember.getEmail());
            existingMember.setName(updatedMember.getName());
            existingMember.setNickname(updatedMember.getNickname());

            return memberRepository.save(existingMember);
        } else {
            throw new EntityNotFoundException("Member not found");
        }
    }

    @Override
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }
}
