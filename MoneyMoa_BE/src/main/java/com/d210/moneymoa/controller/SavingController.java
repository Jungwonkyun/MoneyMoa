package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.LikedSaving;
import com.d210.moneymoa.dto.Saving;
import com.d210.moneymoa.dto.SavingComment;
import com.d210.moneymoa.service.SavingCommentService;
import com.d210.moneymoa.service.SavingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saving")
public class SavingController {

    @Autowired
    SavingService savingService;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @Autowired
    private SavingCommentService savingCommentService;

    // 적금상품 API 정보 저장
    @ApiOperation(value = "DB에 적금상품 API 정보 저장 *상품정보 조회가능할시 호출 금지")
    @GetMapping("/save")
    public ResponseEntity<Map<String,Object>> saveSavingProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        HttpStatus status;

        try{
            savingService.saveSavingProducts();
            status = HttpStatus.OK;
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }

    // 적금상품 API 정보 조회
    @ApiOperation(value = "DB에 저장된 모든 적금상품 정보 조회")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getSavingProductsWithInterestDetails() {

        Map<String, Object> resultMap = new HashMap<>();

        HttpStatus status;

        try {
            List<Saving> savingProducts = savingService.getAllSavingProducts();

            // spcl 문자열을 리스트로 변환
            for (Saving saving : savingProducts) {
                String spclStr = saving.getSpcl();
                saving.setSpclList(Arrays.asList(spclStr.split("\n")));
            }

            status = HttpStatus.OK;
            resultMap.put("products", savingProducts);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }


    // 적금상품 API 상세정보 조회
    @ApiOperation(value = "productCode에 해당하는 적금상품 상세정보 조회")
    @GetMapping("/{productCode}")
    public ResponseEntity<Map<String, Object>> getSavingProductsWithInterestDetails(@PathVariable String productCode) {

        Map<String, Object> resultMap = new HashMap<>();

        HttpStatus status;

        try {
            Saving savingProduct = savingService.getSavingProductWithInterestDetails(productCode);

            // Convert spcl string to list
            String spclStr = savingProduct.getSpcl();
            savingProduct.setSpclList(Arrays.asList(spclStr.split("\n")));

            List<SavingComment> savingComments = savingCommentService.findByProductCode(productCode);

            status = HttpStatus.OK;
            resultMap.put("product", savingProduct);
            resultMap.put("comments", savingComments);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "적금 상품을 '찜'으로 등록")
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> saveLikedSaving(@ApiParam(value = "MemberId와, productcode가 잘못되면 안들어갑니다.")
                                                               @RequestBody LikedSaving likedSaving,
                                                               @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                               @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            likedSaving.setMemberId(memberId);

            savingService.saveLikedSaving(likedSaving);
            status = HttpStatus.OK;
            resultMap.put("likedSaving", likedSaving);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "적금상품에 댓글 작성")
    @PostMapping("/{productCode}/comment")
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable String productCode,
                                                             @RequestBody SavingComment savingComment,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            savingComment.setMemberId(memberId);

            savingCommentService.createSavingComment(productCode, savingComment);
            status = HttpStatus.CREATED;
            resultMap.put("savingComment", savingComment);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "적금상품 댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            savingCommentService.deleteSavingComment(commentId, memberId);
            status = HttpStatus.OK;
            resultMap.put("message", "success");
            resultMap.put("message2", "삭제 되었습니다.");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            status = HttpStatus.FORBIDDEN;
            resultMap.put("message", "fail");
            resultMap.put("message2", "작성자가 아닙니다.");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", "fail");
            resultMap.put("message2", "jwttoken이 잘못되었거나 commentId가 잘못되었습니다.");
        }

        return new ResponseEntity<>(resultMap, status);
    }

    // 클래스 내부에 다음 메서드를 추가
    @ApiOperation(value = "적금상품 댓글 수정")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long commentId,
                                                             @RequestBody SavingComment updateSavingComment,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            savingCommentService.updateSavingComment(commentId, memberId, updateSavingComment);
            status = HttpStatus.OK;
            resultMap.put("message", "success");
            resultMap.put("updateSavingComment", updateSavingComment);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            status = HttpStatus.FORBIDDEN;
            resultMap.put("message", "fail");
            resultMap.put("message2", "작성자가 아닙니다.");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            resultMap.put("message", "fail");
            resultMap.put("message2", "jwttoken이 잘못되었거나 commentId가 잘못되었습니다.");
        }

        return new ResponseEntity<>(resultMap, status);
    }
}




