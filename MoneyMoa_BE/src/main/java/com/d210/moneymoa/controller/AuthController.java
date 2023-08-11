package com.d210.moneymoa.controller;


import com.d210.moneymoa.domain.kakao.KakaoLoginParams;
import com.d210.moneymoa.domain.naver.NaverLoginParams;
import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.MemberService;
import com.d210.moneymoa.service.OAuthLoginServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Api(value = "카카오 로그인과 관련된 Controller")
public class AuthController {
    private final OAuthLoginServiceImpl oAuthLoginService;
    private final AuthTokensGenerator authTokensGenerator;
    private final MemberService memberService;

    @ApiOperation(value = "카카오 소셜 로그인 ",
            notes = "카카오 로그인을 한 후 [redirect URL]?code= + 뒤에 생기는 Kakao에서 보내주는 Token 을 가져와서 보내준다")
    @PostMapping("/kakao")
    public ResponseEntity<Map<String,Object>> loginKakao(@ApiParam(value = "Token") @RequestBody KakaoLoginParams params) {


        Map<String,Object> resultMap = new HashMap<>();

        try{
            AuthTokens authTokens = oAuthLoginService.login(params);
            String jwt = authTokens.getAccessToken();

            resultMap.put("message","success");
            resultMap.put("authtokens",authTokens);
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            Member member = memberService.findMemberById(memberId);
            resultMap.put("member",member);

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
        }

        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
    }


    @ApiOperation(value = "카카오 연결끊기 ")
    @GetMapping("/api/auth/leave")
    public ResponseEntity<?> unconnectKakao() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "네이버 소셜 로그인 ", notes = "네이버 로그인을 한 후 [redirect URL]?code= + 뒤에 생기는 네이버에서 보내주는 Token과" +
            "state 부분을 가져와서 보내준다")
    @PostMapping("/naver")
    public ResponseEntity<Map<String,Object>> loginNaver(@ApiParam(value = "Token, state") @RequestBody NaverLoginParams params) {

        Map<String,Object> resultMap = new HashMap<>();

        try{
            AuthTokens authTokens = oAuthLoginService.login(params);
            String jwt = authTokens.getAccessToken();
            resultMap.put("message","success");
            resultMap.put("authtokens",authTokens);
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            Member member = memberService.findMemberById(memberId);
            resultMap.put("member",member);

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
        }

        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
    }

}
