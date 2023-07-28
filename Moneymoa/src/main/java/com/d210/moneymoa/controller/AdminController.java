package com.d210.moneymoa.controller;


import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Member;
import com.d210.moneymoa.repository.MemberRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: 2023-07-07 Spring Security써서 관리자 role, 일반 유저 role 분리해서 접근하도록 하기 
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Api(value = "관리자 Role에 해당하는 Controller")
public class AdminController {

    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;

    //모든 맴버 정보 리턴
    @ApiOperation(value = "현재 서비스에 가입한 모든 유저의 정보를 보여준다")
    @GetMapping("/loadAllUsers")
    public ResponseEntity<Map<String,Object>> findAll() {

        Map<String,Object> resultMap = new HashMap<>();
        try{
            List<Member>memberList = memberRepository.findAll();
            resultMap.put("message","success");
            resultMap.put("memberList", memberList);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            resultMap.put("memberList", null);
        }
        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

    //id를 통해서 맴버 삭제
    @ApiOperation(value = "관리자가 직접 유저를 삭제" , notes = "아직 사용하지 않는 API")
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String,Object>> deleteUser (@ApiParam(value = "jwt 토큰") @RequestHeader("Authorization") String jwt) throws Exception{

        Map<String,Object> resultMap = new HashMap<>();
        try {
            Long id = authTokensGenerator.extractMemberId(jwt);
            memberRepository.deleteById(id);
            resultMap.put("message","success");
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

    //accessToken으로 유저 정보 찾기
    @ApiOperation(value = "유저의 jwt 토큰을 파라미터로 받아서 member에 대한 객체 정보를 반환",
            notes = "DB를 직접 보지 않고 JWT 토큰만 있으면 객체에 대한 값들을 알 수 있음")
    @GetMapping("/{accessToken}")
    public ResponseEntity<Member> findByAccessToken(@ApiParam(value = "해당 유저의 JWT 토큰") @RequestHeader("Authorization") String jwt) {
        Long memberId = authTokensGenerator.extractMemberId(jwt);
        return ResponseEntity.ok(memberRepository.findById(memberId).orElse(null));
    }

    //id를 가지고 유저 정보 찾기
    @ApiOperation(value = "유저의 id값을 header로 보내서 member에 대한 객체 정보를 반환",
            notes = "앞의 API는 JWT Token으로 이번 API는 유저 ID로만 객체 정보 반환")
    @GetMapping("/personId/{id}")
    public ResponseEntity<Member> findById(@ApiParam(value = "해당 유저의 DB 저장 ID") @PathVariable Long id) {
        return ResponseEntity.ok(memberRepository.findById(id).orElse(null));
    }
}
