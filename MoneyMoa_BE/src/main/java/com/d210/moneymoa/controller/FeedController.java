package com.d210.moneymoa.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.*;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.MemberRepository;
import com.d210.moneymoa.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URL;
import java.util.*;


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
    AuthTokensGenerator authTokensGenerator;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    FeedCommentService feedCommentService;

    @Autowired
    private AmazonS3 s3Client;

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

                // 8월 13일 02:41 코드 추가 : 챌린지의 presentAmount에 depositAmount 더하기
                Challenge challenge = challengeOptional.get();
                challenge.setPresentAmount(challenge.getPresentAmount() + feed.getDepositAmount());

                challengeRepository.save(challenge);

                // 존재하면 새 피드를 생성하고 입력받은 memberId, challengeId로 Feed 객체를 만들어 반환
                Feed newFeed = feedService.createFeed(challengeId, memberId, feed);

                // 피드가 성공적으로 생성되면 HTTP 상태를 CREATE로 변경
                status = HttpStatus.CREATED;
                resultMap.put("feed", newFeed);
                resultMap.put("message", "success");


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

            List<Feed> modifiedFeedList = new ArrayList<>();

            for (Feed feed : feedList) {
                List<FeedFile> feedFiles = feed.getFeedFiles();
                List<String> fileUrls = new ArrayList<>();

                // 각 Feed별로 저장된 이미지에 대해 파일 URL 생성
                for (FeedFile feedFile : feedFiles) {
                    URL fileUrl = s3Client.getUrl("moneymoa-first-bucket", feedFile.getImgPath());
                    fileUrls.add(fileUrl.toString());
                }

                // Feed 객체에 fileUrls 설정
                feed.setFileUrls(fileUrls);

                // 수정된 feed 객체를 modifiedFeedList에 추가
                modifiedFeedList.add(feed);
            }

            resultMap.put("feedList", modifiedFeedList);
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


            for (Feed feed : feedList) {
                List<FeedFile> feedFiles = feed.getFeedFiles();
                List<String> fileUrls = new ArrayList<>();

                // 각 Feed별로 저장된 이미지에 대해 파일 URL 생성
                for (FeedFile feedFile : feedFiles) {
                    URL fileUrl = s3Client.getUrl("moneymoa-first-bucket", feedFile.getImgPath());
                    fileUrls.add(fileUrl.toString());
                }

                // Feed 객체에 fileUrls 설정
                feed.setFileUrls(fileUrls);
            }

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

            List<FeedFile> feedFiles = feed.getFeedFiles();
            List<String> fileUrls = new ArrayList<>();

            // 각 Feed별로 저장된 이미지에 대해 파일 URL 생성
            for (FeedFile feedFile : feedFiles) {
                URL fileUrl = s3Client.getUrl("moneymoa-first-bucket", feedFile.getImgPath());
                fileUrls.add(fileUrl.toString());
            }

            // Feed 객체에 fileUrls 설정
            feed.setFileUrls(fileUrls);

            // 피드 댓글 조회
            List<FeedComment> feedComments = feedCommentService.findByFeedId(feedId);

            resultMap.put("feed", feed);
            resultMap.put("comments", feedComments);
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
            Feed originalFeed = feedService.getFeedById(feedId);
            Integer originalDepositAmount = originalFeed.getDepositAmount();
            Integer updatedDepositAmount = feedService.updateFeed(feedId, updateFeed, memberId);
            if (updateFeed.getChallengeId() != null && updateFeed.getChallengeId().equals(originalFeed.getChallengeId())
                    && !originalDepositAmount.equals(updatedDepositAmount)) {
                Challenge challenge = challengeRepository.findById(updateFeed.getChallengeId())
                        .orElseThrow(() -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));


                challenge.setPresentAmount(challenge.getPresentAmount() - originalDepositAmount);
                challenge.setPresentAmount(challenge.getPresentAmount() + updatedDepositAmount);

                challengeRepository.save(challenge);

            }


            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } catch (Exception e) {            // 예외 발생시 예외 및 HTTP BAD_REQUEST 출력
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


            // 해당 피드의 파일들을 조회
            Feed feed = feedService.getFeedDetail(feedId);
            List<FeedFile> feedFiles = feed.getFeedFiles();

            // 파일들을 삭제
            for (FeedFile feedFile : feedFiles) {
                storageService.deleteFile(feedFile.getImgPath());
            }

            // 피드 삭제
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

    @ApiOperation(value = "피드 댓글 작성", notes = "피드에 댓글 작성합니다.")
    @PostMapping("/comment/{feedId}")
    public ResponseEntity<Map<String, Object>> createFeedComment(@PathVariable Long feedId,
                                                                 @RequestBody FeedComment feedComment,
                                                                 @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                                 @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            //멤버 조회
            Optional<Member> optionalMember = memberRepository.findById(memberId);
            if (!optionalMember.isPresent()) {
                throw new IllegalStateException("존재하지 않는 회원입니다.");
            }
            Member member = optionalMember.get();

            feedComment.setMemberId(memberId);
            feedComment.setNickname(member.getNickname());

            feedCommentService.createFeedComment(feedId, feedComment);
            status = HttpStatus.CREATED;
            resultMap.put("feedComment", feedComment);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "피드 댓글 수정")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long commentId,
                                                             @RequestBody FeedComment editFeedComment,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));
            feedCommentService.updateFeedComment(commentId, memberId, editFeedComment);
            status = HttpStatus.OK;
            resultMap.put("message", "success");
            resultMap.put("updateFeedComment", editFeedComment);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            status = HttpStatus.FORBIDDEN;
            resultMap.put("message", "fail");
            resultMap.put("message2", "작성자 ID가 일치하지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", "fail");
            resultMap.put("message", "로그인 토큰(JWT) 또는 CommentID에 문제가 있습니다.");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "피드 댓글 삭제")
    @DeleteMapping("/comment/delete/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));
            feedCommentService.deleteFeedComment(commentId, memberId);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } catch (IllegalAccessError e) {
            // 예외 발생 시 예외를 출력하고 HTTP 상태를 BAD_REQUEST로 설정
            e.printStackTrace();
            status = HttpStatus.FORBIDDEN;
            // resultMap에 실패 메시지 추가
            resultMap.put("message", "fail");
            resultMap.put("message", "작성자 ID가 일치하지 않습니다");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", "fail");
            resultMap.put("message2", "로그인 토큰(JWT) 또는 CommentID에 문제가 있습니다.");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "피드 검색( #붙여서 검색 할 경우 hashtag 검색)")
    @GetMapping("/search")
    public ResponseEntity<List<Feed>> findByContent(@RequestParam("keyword") String keyword) {
        List<Feed> feeds;

        log.info("키워드");


        if (keyword.startsWith("#")) {
            String hashtag = keyword.substring(1);
            feeds = feedService.findByHashtags(hashtag);
        } else {
            feeds = feedService.findByContent(keyword);
        }
        if (feeds.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(feeds, HttpStatus.OK);
    }

    @ApiOperation(value = "피드 좋아요", notes = "피드 좋아요 버튼입니다. 한번 누르면 true/feedLike 테이블에 저장. 한번 더 누르면 false/좋아요 테이블에서 삭제")
    @PutMapping("/like/{feedId}")
    public ResponseEntity<?> toggleFeedLike(@PathVariable Long feedId, @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;
        log.info("좋아요");

        try {
            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));
            log.info("좋아요 jwt memberId");

            boolean isLiked = feedService.toggleLike(memberId, feedId);
            log.info("좋아요 jwt boolean 통과");
            Feed updateFeed = feedService.getFeedById(feedId);
            log.info("updateFeed feedId");
            status = HttpStatus.OK;
            resultMap.put("message", "success");
            resultMap.put("feed", updateFeed);
            resultMap.put("isLiked", isLiked);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("좋아요 jwt httpStatus ok");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.info(" httpStatus ok");
            resultMap.put("message", "fail");
            resultMap.put("message2", "로그인 토큰(JWT) 또는 FeedID에 문제가 있습니다.");
        }
        return new ResponseEntity<>(resultMap, status);
    }

}


