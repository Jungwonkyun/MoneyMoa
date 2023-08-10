package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Challenge;
import com.d210.moneymoa.dto.Feed;
import com.d210.moneymoa.dto.FeedFile;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.FeedFileService;
import com.d210.moneymoa.service.FeedService;
import com.d210.moneymoa.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Api(value = "Feed Controller", tags = "Feed-Controller")
@RestController
@Slf4j
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    FeedService feedService;

    @Autowired
    FeedFileService feedFileService;

    @Autowired
    StorageService storageService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @Autowired
    private ChallengeRepository challengeRepository;


    // 피드 생성 메서드
    // Swagger API 문서에 Endpoint 정보 추가
    @ApiOperation(value = "피드 생성", notes = "피드 작성합니다.")

    @PostMapping(path = "/create/{challengeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createFeed(
            @PathVariable Long challengeId,
            @RequestPart("feed") String feedString,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
            @RequestHeader("Authorization") String jwt) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Feed feed = objectMapper.readValue(feedString, Feed.class);

        // 결과를 반환할 Map 객체 생성
        Map<String, Object> resultMap = new HashMap<>();
        // HTTP 상태 기본값 설정
        HttpStatus status;

        try {
            // "Bearer " 문자열을 제거하여 JWT 토큰 문자열만 얻음
            jwt = jwt.replace("Bearer ", "");
            // JWT 토큰에서 회원 ID를 추출
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            Optional<Challenge> challengeOptional = challengeRepository.findById(challengeId);
            if (challengeOptional.isPresent()) {
                // 존재하면 새 피드를 생성하고 입력받은 memberId, challengeId로 Feed 객체를 만들어 반환
                Feed newFeed = feedService.createFeed(challengeId, memberId, feed);

                // 피드가 성공적으로 생성되면 HTTP 상태를 CREATE로 변경
                status = HttpStatus.CREATED;

                // 파일이 전달되었다면, 각 파일을 처리하고 피드에 추가합니다.
                if (files != null && files.length > 0) {
                    for (MultipartFile file : files) {
                        String fileName = storageService.uploadFile(file);

                        FeedFile feedFile = new FeedFile();
                        feedFile.setImgPath(fileName);
                        feedFile.setFeed(newFeed);

                        // feedFile 저장 로직은 여기에 구현해야 합니다.
                        feedFileService.saveFeedFile(feedFile);
                    }


                    //resultMap에 생성된 새 피드와 성공 메시지 추가
                    resultMap.put("feed", newFeed);
                    resultMap.put("message", "success");
                    }
                } else {
                    //챌린지가 존재하지 않을 경우 HTTP 상태를 NOT_FOUND로 변경
                    status = HttpStatus.NOT_FOUND;
                    resultMap.put("message", "Challenge not found");
                }
        } catch (Exception e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지를 추가
            resultMap.put("message", "fail");

        }
        // 최종 결과와 설정된 HTTP 상태를 반환하는 ResponseEntity 객체를 반환
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "피드 전체 조회", notes = "피드 전체목록을 조회합니다")
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFeeds(@RequestHeader("Authorization") String jwt) {
        log.info("BoardList 모두 반환");
        HttpStatus status;
        List<Feed> feedList;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            feedList = feedService.getAllFeeds();
            resultMap.put("feedList", feedList);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);

        } catch (Exception e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지를 추가
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "특정 회원의 피드 조회", notes = "특정 회원의 피드 목록을 조회합니다.")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Map<String, Object>> getMemberFeeds(@RequestHeader("Authorization") String jwt,
                                                              @PathVariable Long memberId) {
        log.info("특정 회원의 피드 반환");
        HttpStatus status;
        List<Feed> feedList;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            feedList = feedService.getMemberFeeds(memberId);
            resultMap.put("feedList", feedList);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);

        } catch (Exception e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지를 추가
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "피드 상세 조회", notes = "특정 피드의 상세 정보를 조회합니다.")
    @GetMapping("/detail/{feedId}")
    public ResponseEntity<Map<String, Object>> getFeedDetail(@RequestHeader("Authorization") String jwt,
                                                             @PathVariable Long feedId) {
        log.info("피드 상세 정보 반환");
        HttpStatus status;
        Feed feed;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            feed = feedService.getFeedDetail(feedId);
            resultMap.put("feed", feed);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);

        } catch (Exception e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지를 추가
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "피드 수정", notes = "피드를 수정합니다.")
    @PutMapping("/update/{feedId}")
    public ResponseEntity<Map<String, Object>> updateFeed(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long feedId,
                                                          @RequestBody Feed updateFeed) {
        log.info("피드 수정");
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));
            feedService.updateFeed(feedId, updateFeed, memberId);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생시 예외 및 HTTP BAD_REQUEST 출력
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            //resultMap에 실패 메시지 추가
            resultMap.put("message", "fail");

        }
        return new ResponseEntity<>(resultMap, status);

    }

    @ApiOperation(value = "피드 삭제", notes = "특정 피드를 삭제합니다.")
    @DeleteMapping("/delete/{feedId}")
    public ResponseEntity<Map<String, Object>> deleteFeed(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long feedId) {
        log.info("피드 삭제");
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));
            feedService.deleteFeed(feedId, memberId);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);

        } catch (Exception e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지를 추가
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }



}
