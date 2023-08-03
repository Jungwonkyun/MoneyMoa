package com.d210.moneymoa.controller;


import com.d210.moneymoa.domain.kakao.KakaoLoginParams;
import com.d210.moneymoa.domain.naver.NaverLoginParams;
import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
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

    @ApiOperation(value = "카카오 소셜 로그인 ",
            notes = "카카오 로그인을 한 후 [redirect URL]?code= + 뒤에 생기는 Kakao에서 보내주는 Token 을 가져와서 보내준다")
    @PostMapping("/kakao")
    public ResponseEntity<Map<String,Object>> loginKakao(@ApiParam(value = "Token") @RequestBody KakaoLoginParams params) {


        Map<String,Object> resultMap = new HashMap<>();

        try{
            AuthTokens authTokens = oAuthLoginService.login(params);
            resultMap.put("message","success");
            resultMap.put("authtokens",authTokens);
            // resultMap.put("member",oAuthLoginService.findLogin(authTokens));

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
        }

        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);


        //return ResponseEntity.ok(oAuthLoginService.login(params));
    }



    @ApiOperation(value = "네이버 소셜 로그인 ", notes = "네이버 로그인을 한 후 [redirect URL]?code= + 뒤에 생기는 네이버에서 보내주는 Token과" +
            "state 부분을 가져와서 보내준다")
    @PostMapping("/naver")
    public ResponseEntity<Map<String,Object>> loginNaver(@ApiParam(value = "Token, state") @RequestBody NaverLoginParams params) {

        Map<String,Object> resultMap = new HashMap<>();

        try{
            AuthTokens authTokens = oAuthLoginService.login(params);
            resultMap.put("message","success");
            resultMap.put("authtokens",authTokens);
            // resultMap.put("member",oAuthLoginService.findLogin(authTokens));

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
        }

        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
        //return ResponseEntity.ok(oAuthLoginService.login(params));
    }


//    @ApiOperation(value = "접속 유저의 JWT 토큰을 통해서 해당 유저의 ID를 리턴하는 API",
//                  notes = "전송 형식: Authorization : Bearer $[JWT Token]")
//    @PostMapping("/findid")
//    public ResponseEntity<Long> extractIdByAccessToken(@ApiParam(value = "해당 유저의 JWT 토큰") @RequestHeader("Authorization") String jwt){
//        String accessToken = jwt.replace("Bearer ", "");
//        return ResponseEntity.ok(authTokensGenerator.extractMemberId(accessToken));
//    }

}
