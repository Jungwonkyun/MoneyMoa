package com.d210.moneymoa.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.d210.moneymoa.domain.oauth.AuthTokens;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.AuthToken;
import com.d210.moneymoa.dto.LoginInfo;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.dto.MemberUpdateInfo;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.MemberServiceImpl;
import com.d210.moneymoa.service.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(value = "일반유저가 할 수 있는 역할에 대한 Controller")
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    AuthTokensGenerator authTokensGenerator;
    @Autowired
    StorageService storageService;
    @Autowired
    private AmazonS3 s3Client;


    @ApiOperation(value = "일반 로그인 처리" , notes = "유저 정보가 있으면 jwt 토큰이랑 유저 정보 반환 " +
            "없다면 fail 메세지")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> postLogin(@ApiParam(value = "이메일, 패스워드 입력") @RequestBody LoginInfo loginInfo) throws IOException {

        System.out.println("로그인 제대로 들어왔음");

        Map<String,Object> resultMap = new HashMap<>();
        Member member = null;
        HttpStatus status;

        try{
            member = memberService.findLoginMember(loginInfo.getEmail(),loginInfo.getPassword());
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
            status = HttpStatus.OK;
        }catch (Exception e){
            //로그인 실패
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
            resultMap.put("jwt token", "fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "회원탈퇴",
            notes = "논리적 탈퇴 valid = 1 -> 0으로 변경")
    @DeleteMapping("/quitService")
    public ResponseEntity<?> quitService(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")  @RequestHeader("Authorization") String jwt){

        jwt =  jwt.replace("Bearer ", "");
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;

        try{
            Long id = authTokensGenerator.extractMemberId(jwt);
            memberService.quitService(id);
            resultMap.put("message","success");
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);

    }

    @ApiOperation(value = "유저 회원가입", notes = "Member 객체를 프론트에서 제공" +
            "여기서 email, name, nickname, password, gender, birthday 만 보내고 나머지는 건들지 않아도 됩니다"  )
    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signUp (@RequestBody Member member){

        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;

        try{
            AuthTokens authTokens = memberService.login(member);
            resultMap.put("message","success");
            resultMap.put("member",member);
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("member",null);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "유저 로그아웃", notes = "유저 jwt 토큰을 보내주면 블랙리스트 방식으로 jwt 토큰을 말소하고 성공 여부를 반환")
    @GetMapping ("/loggout")
    public ResponseEntity<Map<String,Object>> logout (@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")  @RequestBody AuthToken jwtTokens){

        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;

        try{
            AuthToken authToken = memberService.logout(jwtTokens);
            resultMap.put("message","success");
            resultMap.put("expired jwt", authToken);
            status = HttpStatus.OK;
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "비밀번호 찾기" , notes = "requestbody로 유저가 찾으려는 이메일을 보낸다 -> 유효한 이메일이면 success고 유저의 이메일로 임시 비밀번호를 제공")
    @PostMapping("/findpassword")
    public ResponseEntity<?> findPassword(@ApiParam(value = "유저 이메일")@RequestBody String email)throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;
        email = email.replaceAll("\"","");
        log.info(email);
        
        try {
            Member member = memberService.findMemberByEmail(email);
            log.info("member는 정상적으로 가져옴: " +  member.toString());
            String authCode = memberService.sendEmail2(email);
            log.info("이메일 인증까지 받아서 코드 보냄: " +  authCode);
            member.setPassword(authCode);

//            String encPw = encoder.encode(authCode);
//            member.setPassword(encPw);
            memberRepository.save(member);
            log.info("객체 저장 완료!!");

            resultMap.put("message", "success");
            resultMap.put("member", member);
            status = HttpStatus.OK;

        }catch (Exception e){
            resultMap.put("message", "fail");
            status = HttpStatus.BAD_REQUEST;
            e.printStackTrace();
        }

        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }

    @ApiOperation(value = "유저 이메일 인증", notes = "일반 회원가입시 이메일을 인증하는 api 리턴값 : 이메일 인증 때 사용할 임의의 문자열")
    @PostMapping("/emailauth")
    public ResponseEntity<?> emailAuth(@ApiParam(value = "유저 이메일")@RequestBody String email)throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;
        String message = "";

        email = email.replaceAll("\"","");
        log.info(email);

        try {
            message = "success";

            if(memberService.findMemberByEmail(email)!= null){
                message = "already in Database";
                resultMap.put("message", message);
                return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
            }

            String authCode = memberService.sendEmail(email);
            if(memberService.findMemberByEmail(email)!= null){
                message = "already in Database";
            }

            resultMap.put("message", message);
            resultMap.put("emailAuth", authCode);
            status = HttpStatus.OK;

        }catch (Exception e){
            resultMap.put("message", "fail");
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }

     //id로 유저 정보 찾기
    @ApiOperation(value = "유저의 jwt 토큰을 파라미터로 받아서 member에 대한 객체 정보를 반환",
            notes = "DB를 직접 보지 않고 유저 ID를 통해 객체에 대한 값들을 알 수 있음")
    @GetMapping("/myinfo")
    public ResponseEntity<Map<String,Object>> findByAccessToken(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")  @RequestHeader("Authorization") String jwt) {
        jwt =  jwt.replace("Bearer ", "");
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            Member mem = memberService.findMemberById(memberId);
            resultMap.put("message","success");
            resultMap.put("sombody",mem);
            status = HttpStatus.OK;

        }catch (Exception e){
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
            e.printStackTrace();
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "유저의 jwt 토큰을 파라미터로 받아서 member에 대한 객체 정보를 반환",
            notes = "DB를 직접 보지 않고 유저 ID를 통해 객체에 대한 값들을 알 수 있음")
    @GetMapping("/sombodyinfo/{memberId}")
    public ResponseEntity<Map<String,Object>> sombodyInfo(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")  @RequestHeader("Authorization") String jwt, @PathVariable Long memberId) {
        jwt =  jwt.replace("Bearer ", "");
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try{
            Member mem = memberService.findMemberById(memberId);
            resultMap.put("message","success");
            resultMap.put("sombody",mem);
            status = HttpStatus.OK;

        }catch (Exception e){
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
            e.printStackTrace();
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }



    @ApiOperation(value = "멤버 정보 업데이트", notes = "멤버의 정보를 수정하고 업데이트된 멤버 정보를 반환합니다.")
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMember(@ApiParam(value = "수정할 멤버 정보") @RequestPart("MemberUpdateInfo") MemberUpdateInfo memberUpdate,
                                                            @ApiParam(value = "Bearer ${jwt}") @RequestHeader("Authorization") String jwt,
                                                            @RequestPart(value = "file", required = false) MultipartFile[] file) {
        jwt = jwt.replace("Bearer ", "");
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            Member updatedMember = null;
            //파일이 있으면
            if (file != null && file.length > 0) {
                String fileName = storageService.uploadFile(file[0]);
                URL fileUrl = s3Client.getUrl("moneymoa-first-bucket", fileName);
                updatedMember = memberService.updateMember(memberUpdate,memberId,fileUrl.toString());
            }
            else{
                //파일이 없을 때
                updatedMember = memberService.updateMember(memberUpdate,memberId,"null");
            }

            resultMap.put("message","success");
            resultMap.put("updatedMember",updatedMember);
            status = HttpStatus.OK;


        }catch (Exception e){
            resultMap.put("message","success");
            status = HttpStatus.BAD_REQUEST;

          e.printStackTrace();
        }
        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "비밀번호 확인로직")
    @PostMapping ("/checkpassword")
    public ResponseEntity<Map<String,Object>> logout (  @RequestBody String password, @RequestHeader("Authorization")String jwt){

        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;
        jwt = jwt.replace("Bearer ","");
        password = password.replaceAll("\"","");

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            
            boolean check = memberService.checkPassword(password,memberId);

            if(check){
                resultMap.put("message","success");
                status = HttpStatus.OK;
            }else{
                resultMap.put("message","Not Matched Password!!");
                status = HttpStatus.OK;
            }

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }
}

