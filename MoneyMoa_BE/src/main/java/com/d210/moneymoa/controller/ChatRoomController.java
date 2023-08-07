package com.d210.moneymoa.controller;

// import 생략...


import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.ChatRoom;
import com.d210.moneymoa.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// TODO: 2023-08-04 REST Controller로 바꿔야함 
@RequiredArgsConstructor
@Controller
@RequestMapping("api/chat")
public class ChatRoomController {

    //private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomService chatRoomService;

    @Autowired
    AuthTokensGenerator authTokensGenerator;

    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    //모든 채팅방 리턴
//    @GetMapping("/roomlist")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>>  roomlist() {
//
//        HashMap<String, Object>resultMap = new HashMap<>();
//        HttpStatus status;
//        String messege = "";
//
//        try{
//            List<ChatRoom> chatRoomList = chatRoomService.findAllRoom();
//            messege = "success";
//            status = HttpStatus.OK;
//            resultMap.put("message", messege);
//            resultMap.put("roomList", chatRoomList);
//        }catch (Exception e){
//            messege = "fail";
//            resultMap.put("message", "message");
//            status = HttpStatus.BAD_REQUEST;
//        }
//
//        return new ResponseEntity<Map<String,Object>>(resultMap,status);
//    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> roomlist() {
        return chatRoomService.findAllRoom();
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomService.createChatRoom(name);
    }


    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

//    @GetMapping("/room/enter/{roomId}")
//    public void roomDetail(@PathVariable String roomId, @RequestHeader("Authorization") String jwt) {
//
//        jwt =  jwt.replace("Bearer ", "");
//        Long memberId = authTokensGenerator.extractMemberId(jwt);
//
//        chatRoomService.enterChatRoom();
//    }


    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomService.findRoomById(roomId);
    }


    @GetMapping("/room/info/{roomId}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> roomInfoApi(@PathVariable String roomId) {

        Map<String,Object>resultMap = new HashMap<>();
        HttpStatus status;
        String message = "";

        try{
            message = "success";
            ChatRoom chatRoom = chatRoomService.findRoomById(roomId);
            resultMap.put("message", message);
            resultMap.put("chatRoom Info", chatRoom);
            status = HttpStatus.OK;

        }catch (Exception e){
            e.printStackTrace();

            message = "fail";
            resultMap.put("message", message);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<Map<String,Object>>(resultMap,status);
    }


}