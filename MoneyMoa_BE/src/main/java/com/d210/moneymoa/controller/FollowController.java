package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Follows;
import com.d210.moneymoa.service.FollowServiceImpl;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    AuthTokensGenerator authTokensGenerator;

    @Autowired
    FollowServiceImpl followService;

    @ApiParam(value = "현재 유저가 특정 유저를 팔로잉")
    @PostMapping("/following")
    public ResponseEntity<Map<String, Object>> followMember(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                                @RequestHeader ("Authorization") String jwt, @RequestBody Long toMemberId){

        jwt =  jwt.replace("Bearer ", "");
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;


        try{
            Long fromMemberId = authTokensGenerator.extractMemberId(jwt);
            boolean check = followService.alreadyFollow(fromMemberId, toMemberId);
            if(check){
                resultMap.put("message","alreadyFollow");
                return new ResponseEntity<Map<String,Object>>(resultMap,HttpStatus.OK);
            }

            Follows follows = followService.followMember(fromMemberId,toMemberId);
            resultMap.put("message","success");
            resultMap.put("follow info", follows);
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }

    @ApiParam(value = "현재 유저가 특정 유저를 팔로잉취소")
    @DeleteMapping("/unfollowing/{id}")
    public ResponseEntity<Map<String, Object>> unfollowMember(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                                  @RequestHeader ("Authorization") String jwt, @PathVariable Long id){

        jwt =  jwt.replace("Bearer ", "");
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;


        try{
            Long fromMemberId = authTokensGenerator.extractMemberId(jwt);
            followService.unFollowMember(fromMemberId,id);
            resultMap.put("message","success");
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }

    @ApiParam(value = "현재 유저의 팔로잉 리스트")
    @GetMapping("/myfollowing")
    public ResponseEntity<Map<String, Object>> myFollowingList(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                              @RequestHeader ("Authorization") String jwt){

        jwt =  jwt.replace("Bearer ", "");
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;


        try{
            Long id = authTokensGenerator.extractMemberId(jwt);
            List<Follows> myfollowing = followService.myFollowingList(id);
            resultMap.put("message","success");
            resultMap.put("my following list", myfollowing);
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiParam(value = "현재 유저의 팔로워 리스트")
    @GetMapping("/myfollower")
    public ResponseEntity<Map<String, Object>> myFollowerList(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                              @RequestHeader ("Authorization") String jwt){

        jwt =  jwt.replace("Bearer ", "");
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;


        try{
            Long id = authTokensGenerator.extractMemberId(jwt);
            List<Follows> myfollower = followService.myFollowerList(id);
            resultMap.put("message","success");
            resultMap.put("my follower list", myfollower);
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiParam(value = "상대방 유저의 팔로잉 리스트")
    @PostMapping("/memberfollowlist/{id}")
    public ResponseEntity<Map<String, Object>> memberFollowingList(@ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                               @RequestHeader ("Authorization") String jwt, @ApiParam(value = "상대방 id")@RequestBody Long id){

        jwt =  jwt.replace("Bearer ", "");
        Map<String,Object> resultMap = new HashMap<>();
        HttpStatus status;


        try{
            List<Follows> myfollowing = followService.myFollowingList(id);
            resultMap.put("message","success");
            resultMap.put("membersFollowlist", myfollowing);
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("message","fail");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }

}
