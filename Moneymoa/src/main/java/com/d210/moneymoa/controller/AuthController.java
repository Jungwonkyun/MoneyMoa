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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Api(value = "카카오 로그인과 관련된 Controller")
public class AuthController {
    private final OAuthLoginServiceImpl oAuthLoginService;
    private final AuthTokensGenerator authTokensGenerator;

    @ApiOperation(value = "카카오 소셜 로그인 ", notes = "")
    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@ApiParam(value = "카카오 로그인을 한 후 [redirect URL]?code=" +
            "뒤에 생기는 Kakao에서 보내주는 Token") @RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @ApiOperation(value = "네이버 소셜 로그인 ", notes = "")
    @PostMapping("/naver")
    public ResponseEntity<AuthTokens> loginNaver(@RequestBody NaverLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }


//    @ApiOperation(value = "접속 유저의 JWT 토큰을 통해서 해당 유저의 ID를 리턴하는 API",
//                  notes = "전송 형식: Authorization : Bearer $[JWT Token]")
//    @PostMapping("/findid")
//    public ResponseEntity<Long> extractIdByAccessToken(@ApiParam(value = "해당 유저의 JWT 토큰") @RequestHeader("Authorization") String jwt){
//        String accessToken = jwt.replace("Bearer ", "");
//        return ResponseEntity.ok(authTokensGenerator.extractMemberId(accessToken));
//    }

}
