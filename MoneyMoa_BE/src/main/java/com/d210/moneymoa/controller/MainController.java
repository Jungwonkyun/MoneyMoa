package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.service.MemberServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    AuthTokensGenerator authTokensGenerator;

    @ApiOperation(value = "메인화면")
    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> main() throws IOException {
        Map<String,Object>resultMap = new HashMap<>();
        resultMap.put("message","success");
        return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
    }

}
