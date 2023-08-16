package com.d210.moneymoa.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.Challenge;
import com.d210.moneymoa.dto.ChallengeFile;
import com.d210.moneymoa.repository.ChallengeRepository;
import com.d210.moneymoa.repository.FeedRepository;
import com.d210.moneymoa.repository.MemberRepository;

import com.d210.moneymoa.service.ChallengeFileService;
import com.d210.moneymoa.service.ChallengeService;
import com.d210.moneymoa.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Challenge Controller", tags = "Challenge-Controller")
@RestController
@Slf4j
@RequestMapping("api/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    ChallengeFileService challengeFileService;

    // 챌린지 생성 메서드
    // Swagger API 문서에 Endpoint 정보 추가
    @ApiOperation(value = "챌린지 생성", notes = "챌린지 작성합니다.")
    // POST 방식으로 "/create" URL에 매핑
    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // 메서드의 반환 타입은 ResponseEntity 객체이며, 요청 본문에서 Challenge 데이터를 파싱하고 인증 헤더를 사용합니다.
    public ResponseEntity<Map<String, Object>> createChallenge(
            @RequestPart("challenge") String challengeString,
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Challenge challenge = objectMapper.readValue(challengeString, Challenge.class);

        // 결과를 반환할 Map 객체 생성
        Map<String, Object> resultMap = new HashMap<>();
        // HTTP 상태 기본값 설정
        HttpStatus status;

        try {
            // "Bearer " 문자열을 제거하여 JWT 토큰 문자열만 얻음
            jwt = jwt.replace("Bearer ", "");
            // JWT 토큰에서 회원 ID를 추출
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            // 새 챌린지를 생성하고 입력받은 memberId로 Challenge 객체를 만들어 반환
            Challenge newChallenge = challengeService.createChallenge(challenge, memberId);

            // 파일이 전달되었다면, 각 파일을 처리하고 챌린지에 추가합니다.
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String fileName = storageService.uploadFile(file);

                    ChallengeFile challengeFile = new ChallengeFile();
                    challengeFile.setImgPath(fileName);
                    challengeFile.setChallenge(newChallenge);

                    // challengeFile 저장 로직은 여기에 구현해야 합니다.
                    challengeFileService.saveChallengeFile(challengeFile);
                }
            }

            // 챌린지가 성공적으로 생성되면 HTTP 상태를 CREATED로 변경
            status = HttpStatus.CREATED;
            // resultMap에 생성된 새 챌린지와 성공 메시지를 추가
            resultMap.put("challenge", newChallenge);
            resultMap.put("message", "success");

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

    @ApiOperation(value = "멤버의 챌린지 목록 조회", notes = "특정 멤버의 챌린지 목록을 조회합니다.")
    @GetMapping("/list/{memberId}")
    public ResponseEntity<Map<String, Object>> getMemberChallenges(@PathVariable Long memberId,
                                                                   @RequestHeader("Authorization") String jwt) {
        log.info("멤버 챌린지 목록 반환");
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Challenge> challenges;

        try {
            challenges = challengeService.getMemberChallenges(memberId);

            for (Challenge challenge : challenges) {
                List<ChallengeFile> challengeFiles = challenge.getChallengeFiles();
                List<String> fileUrls = new ArrayList<>();

                // 각 Challenge별로 저장된 이미지에 대해 파일 URL 생성
                for (ChallengeFile challengeFile : challengeFiles) {
                    URL fileUrl = s3Client.getUrl("moneymoa-first-bucket", challengeFile.getImgPath());
                    fileUrls.add(fileUrl.toString());
                }

                // Challenge 객체에 fileUrls 설정
                challenge.setFileUrls(fileUrls);
            }

            resultMap.put("challenges", challenges);
            resultMap.put("message", "succsess");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);

        } catch (Exception e) {
            //예외 발생시 예외 출력 후 BAD_REQUEST 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "챌린지 상세조회", notes = "특정 챌린지의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getChallenge(@PathVariable Long id,
                                                            @RequestHeader("Authorization") String jwt) {
        log.info("챌린지 상세 정보 조회");
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Challenge challenge;

        try {
            challenge = challengeService.getChallenge(id);

            // 챌린지에 연결된 파일 목록을 가져옵니다.
            List<ChallengeFile> challengeFiles = challenge.getChallengeFiles();
            List<String> fileUrls = new ArrayList<>();

            // 각 챌린지별로 저장된 이미지에 대해 파일 URL 생성
            for (ChallengeFile challengeFile : challengeFiles) {
                URL fileUrl = s3Client.getUrl("your-s3-bucket-name", challengeFile.getImgPath());
                fileUrls.add(fileUrl.toString());
            }

            // Challenge 객체에 fileUrls 설정
            challenge.setFileUrls(fileUrls);

            resultMap.put("challenge", challenge);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생 시 예외 출력 및 BAD_REQUEST 설정
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            //resultMap 실패 메시지
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "챌린지 업데이트", notes = "챌린지 정보를 업데이트 합니다.")
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateChallenge(@RequestHeader("Authorization") String jwt,
                                                               @PathVariable Long id,
                                                               @RequestBody Challenge challengeInfo) {
        log.info("챌린지 업데이트");
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));
            challengeService.updateChallenge(id, challengeInfo, memberId);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 발생시 예외 출력, BAD_REQUEST
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지 추가
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "챌린지 삭제", notes = "챌린지를 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteChallenge(@PathVariable Long id,
                                                               @RequestHeader("Authorization") String jwt) {
        log.info("챌린지 삭제");
        HttpStatus status;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            jwt = jwt.replace("Bearer ", "");

            Long memberId = authTokensGenerator.extractMemberId(jwt.replace("Bearer ", ""));

            // 해당 챌린지의 파일들을 조회
            Challenge challenge = challengeService.getChallenge(id);
            List<ChallengeFile> challengeFiles = challenge.getChallengeFiles();

            // 파일들을 삭제
            for (ChallengeFile challengeFile : challengeFiles) {
                storageService.deleteFile(challengeFile.getImgPath());
            }

            // 챌린지 삭제
            challengeService.deleteChallenge(id, memberId);
            resultMap.put("message", "success");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } catch (Exception e) {
            //예외 발생시 예외 출력 및 BAD_REQUEST
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            // resultMap에 실패 메시지 추가
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }
    /*

    @ApiOperation(value = "챌린지 상세 조회", notes = "챌린지을 상세 조회합니다")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findFeed(@ApiParam(value = "챌린지 id", required = true) @PathVariable final Long id,
                             @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        return Response.success(challengeService.findChallenge(id));
    }


    @ApiOperation(value = "챌린지 수정", notes = "챌린지을 수정합니다.")
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateChallenge(@PathVariable Long id, @RequestBody Challenge req,
                                    @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) throws ChallengeAuthorizationException {
        jwt = jwt.replace("Bearer ", "");

        return Response.success(challengeService.updateChallenge(id, req, jwt));
    }

    @ApiOperation(value = "챌린지 삭제", notes = "챌린지을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteChallenge(@ApiParam(value = "챌린지 id", required = true) @PathVariable Long id, @ApiParam(value = "Bearer ${jwt token} 형식으로 전송") @RequestHeader("Authorization") String jwt) {
        jwt = jwt.replace("Bearer ", "");
        challengeService.deleteChallenge(id, jwt);
        return Response.success("게시물이 삭제되었습니다.");
    }

    */
}

