package com.d210.moneymoa.controller;


import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.MemberServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Api(value = "일반유저가 할 수 있는 역할에 대한 Controller")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    AuthTokensGenerator authTokensGenerator;


    @ApiOperation(value = "일반 로그인 처리")
    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> getLogin() throws IOException {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("message","success");

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }


    @ApiOperation(value = "일반 로그인 처리")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> postLogin(@RequestBody Map<String,String>loginInfo) throws IOException {

        Map<String,Object> resultMap = new HashMap<>();
        Member member = null;

        try{
            member = memberService.findLoginMember(loginInfo.get("email"),loginInfo.get("password"));
        }catch (Exception e){
            e.printStackTrace();
        }

        if(member == null){
            resultMap.put("message","fail");
            resultMap.put("member",null);
            resultMap.put("jwt token", "fail");
            return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
        }

        try{
            //로그인 성공
            AuthTokens authTokens = memberService.login(member);
            resultMap.put("message","success");
            resultMap.put("member",member);
            resultMap.put("jwt token", authTokens);
        }catch (Exception e){
            //로그인 실패
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
            resultMap.put("jwt token", "fail");
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

    @ApiOperation(value = "회원탈퇴",
            notes = "논리적 탈퇴 valid = 1 -> 0으로 변경")
    @DeleteMapping("/quitService")
    public ResponseEntity<?> quitService(@RequestHeader("Authorization") String jwt){

        Map<String,Object> resultMap = new HashMap<>();

        try{
            Long id = authTokensGenerator.extractMemberId(jwt);
            memberService.quitService(id);
            resultMap.put("message","success");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);

    }

    @ApiOperation(value = "유저 회원가입", notes = "Member 객체를 프론트에서 제공")
    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signUp (@ApiParam(value = "email,nickname,password 제공") @RequestBody Member member){

        Map<String,Object> resultMap = new HashMap<>();

        try{
            AuthTokens authTokens = memberService.login(member);
            resultMap.put("message","success");
            resultMap.put("member",member);
            resultMap.put("jwt token", authTokens);

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
            resultMap.put("jwt token", "fail");
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

    // TODO: 2023-07-10 유저 jwt 토큰 redis, 블랙리스트 이용해서 말소시키기
    @ApiOperation(value = "유저 로그아웃")
    @GetMapping ("/loggout")
    public ResponseEntity<Map<String,Object>> logout (@RequestHeader("Authorization") String jwt){

        Map<String,Object> resultMap = new HashMap<>();

        try{
            resultMap.put("message","success");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

    @PostMapping("/findpassword")
    public ResponseEntity<?> findPassword(@RequestBody String email)throws Exception{
        Map<String,Object> resultMap = new HashMap<>();



        Member member = memberService.findMemberByEmail(email);
        memberService.mailSend(email,member.getPassword());
        return new ResponseEntity<Integer>(1, HttpStatus.OK);

    }

    @ApiOperation(value = "해당 유저의 닉네임을 설정하는 API",
            notes = "jwt token은 header로 바꿀 nickname은 body로")
    @PutMapping("/makenickname")
    public ResponseEntity<Map<String,Object>> makeUserMadeNickName(@ApiParam(value = "유저 jwt 토큰") @RequestHeader("Authorization") String jwt,
                                                       @ApiParam(value = "바꾸고 싶은 nickname (map 형식)")@RequestBody String nickname){
        Map<String,Object> resultMap = new HashMap<>();

        try {
            String accessToken = jwt.replaceAll("Bearer ", "");
            Long memberId = memberService.setUserNickName(nickname, accessToken);
            Member member = memberRepository.findById(memberId).orElse(null);
            resultMap.put("message","success");
            resultMap.put("member",member);

        }catch(Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

}
