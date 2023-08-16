package com.d210.moneymoa.controller;

// import 생략...


import com.amazonaws.services.s3.AmazonS3;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.*;
import com.d210.moneymoa.service.ChatRoomService;
import com.d210.moneymoa.service.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Autowired
    AuthTokensGenerator authTokensGenerator;

    @Autowired
    StorageService storageService;

    @Autowired
    private AmazonS3 s3Client;

    //모든 채팅방 리턴
    @ApiOperation(value = "채팅방 전체보기", notes = "모든 채팅방 정보를 DB에서 가져다가 리턴")
    @GetMapping("/roomlist")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> roomlistDB() {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";

        try{
            List<ChatRoomDto> chatRoomList = chatRoomService.findAllRoomFromDB();
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("roomList", chatRoomList);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }

    @ApiOperation(value = "내가 참여한 DM방 전체보기")
    @GetMapping("/dmlist")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> DmListDB(@ApiParam(value = "Bearer ${jwt token}") @RequestHeader("Authorization") String jwt) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";

        try{
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            List<DirectMessageRoom> dmList = chatRoomService.findAllDMFromDB(memberId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("dmList", dmList);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


//    @ApiOperation(value = "채팅방 생성하기", notes = "생성할 채팅방 정보 입력하고 생성")
//    @PostMapping("/room/create")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> createRoom(@RequestBody ChatRoomDto inputChatRoomDto) {
//
//        HashMap<String, Object>resultMap = new HashMap<>();
//        HttpStatus status;
//        String messege = "";
//
//        try{
//            ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(inputChatRoomDto);
//            messege = "success";
//            status = HttpStatus.OK;
//            resultMap.put("message", messege);
//            resultMap.put("CreatedChatroom",chatRoomDto);
//        }catch (Exception e){
//            messege = "fail";
//            resultMap.put("message", "message");
//            status = HttpStatus.BAD_REQUEST;
//        }
//
//        return new ResponseEntity<Map<String,Object>>(resultMap,status);
//    }


    @ApiOperation(value = "유저가 채팅방에 입장", notes = "유저가 방에 들어오면 방 구독 정보를 DB에 저장한다")
    @GetMapping("/room/enter/{roomId}")
    public ResponseEntity<Map<String, Object>> EnterRoom(@PathVariable String roomId, @ApiParam(value = "Bearer ${jwt token}") @RequestHeader("Authorization") String jwt) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";
        jwt =  jwt.replace("Bearer ", "");

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            MemberChatroomSubInfo memberChatroomSubInfo = chatRoomService.enterChatRoom(memberId, roomId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("Member sub Chatroom",memberChatroomSubInfo);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "현재 채팅방 정보보기")
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> roomInfo(@PathVariable String roomId) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";

        try{
            ChatRoomDto chatRoomDto = chatRoomService.findRoomByRoomId(roomId);
            List<ChatMessageDto>chatmessages = chatRoomService.getChatMessages(roomId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("chatroomInfo",chatRoomDto);
            resultMap.put("chatMessages" , chatmessages);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }

    @ApiOperation(value = "검색하고 싶은 방 이름으로 채팅방 찾기")
    @PostMapping("/room/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchRoom(
            @ApiParam(value = "검색어") @RequestBody Map<String, String> requestBody) {

        String name = requestBody.get("name");

        HashMap<String, Object>resultMap = new HashMap<>();

        HttpStatus status;
        String messege = "";
        // 로그 찍기
        log.info("Request name: " + name);

        try{
            List<ChatRoomDto> chatRoomDto = chatRoomService.findRoomByName(name);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);

            log.info("ChatRoomDTO: " + chatRoomDto);

            resultMap.put("chatroomInfo",chatRoomDto);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", messege);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "해당 방을 구독중인 사람들 목록 리스트")
    @GetMapping("/room/members/{roomId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> chatRoomMembers(@ApiParam(value = "방 id") @PathVariable String roomId,
                                                               @ApiParam(value = "Bearer ${jwt token}") @RequestHeader("Authorization") String jwt) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";
        jwt =  jwt.replace("Bearer ", "");

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            List<MemberChatroomSubInfo>subMemberList = chatRoomService.chatRoomMembers(roomId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("MemberwhoSubThisChatroom",subMemberList);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }



    @ApiOperation(value = "해당 채팅방 나가기", notes = "나가려는 채팅방에 대한 구독해제")
    @DeleteMapping("/room/out")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> chatRoomOut(@ApiParam(value = "방 id") @RequestBody String roomId,
                                                               @ApiParam(value = "Bearer ${jwt token}") @RequestHeader("Authorization") String jwt) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";
        jwt =  jwt.replace("Bearer ", "");

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            chatRoomService.getOutchatRoom(memberId, roomId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }



    @ApiOperation(value = "Direct방 만들고 입장", notes = "생성할 채팅방 정보 입력하고 생성")
    @PostMapping("/room/createdm")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendDirectMessage(@RequestHeader("Authorization")String jwt, @RequestBody Long sendMemberId) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";
        jwt =  jwt.replace("Bearer ", "");

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            if(chatRoomService.alreadyCreated(memberId, sendMemberId)){
                messege = "alreadyCreated";
                resultMap.put("message", messege);
                status = HttpStatus.OK;
                return new ResponseEntity<Map<String,Object>>(resultMap,status);
            }

            List<MemberChatroomSubInfo> SubList = chatRoomService.sendDirectMessage(memberId,sendMemberId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("subList",SubList);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "유저가 DM방에 입장", notes = "유저가 방에 들어오면 DM방 정보를 DB에 저장한다")
    @GetMapping("/room/enterdm/{roomId}")
    public ResponseEntity<Map<String, Object>> EnterDM(@PathVariable String roomId, @ApiParam(value = "Bearer ${jwt token}") @RequestHeader("Authorization") String jwt) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";
        jwt =  jwt.replace("Bearer ", "");

        try{
            Long memberId = authTokensGenerator.extractMemberId(jwt);
            MemberChatroomSubInfo memberChatroomSubInfo = chatRoomService.enterDMRoom(memberId, roomId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("subDMroom",memberChatroomSubInfo);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "현재 채팅방 정보보기")
    @GetMapping("/room/dm/{roomId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> DMroomInfo(@PathVariable String roomId) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";

        try{
            DirectMessageRoom DMRoom = chatRoomService.findDMRoomByRoomId(roomId);
            List<ChatMessageDto>chatmessages = chatRoomService.getChatMessages(roomId);
            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("DMRoomInfo",DMRoom);
            resultMap.put("chatMessages" , chatmessages);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


    @ApiOperation(value = "채팅방 생성하기", notes = "생성할 채팅방 정보 입력하고 생성")
    @PostMapping(path = "/room/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createRoom(@RequestPart("chatRoom") ChatRoomDto inputChatRoomDto,
                                                          @RequestPart(value = "file", required = false) MultipartFile[] file) {

        HashMap<String, Object>resultMap = new HashMap<>();
        HttpStatus status;
        String messege = "";

        try{
            ChatRoomDto chatRoomDto = null;

            if (file != null && file.length > 0) {
                String fileName = storageService.uploadFile(file[0]);
                URL fileUrl = s3Client.getUrl("moneymoa-first-bucket", fileName);
                chatRoomDto = chatRoomService.createChatRoomWithFile(inputChatRoomDto,fileUrl.toString());
            }
            else{
                chatRoomDto = chatRoomService.createChatRoom(inputChatRoomDto);
            }

            messege = "success";
            status = HttpStatus.OK;
            resultMap.put("message", messege);
            resultMap.put("CreatedChatroom",chatRoomDto);
        }catch (Exception e){
            messege = "fail";
            resultMap.put("message", "message");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }

}



