package com.d210.moneymoa.controller;

import com.d210.moneymoa.domain.oauth.AuthTokensGenerator;
import com.d210.moneymoa.dto.CmaComment;

import com.d210.moneymoa.dto.LikedCma;
import com.d210.moneymoa.service.CmaCommentService;
import com.d210.moneymoa.service.CmaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/cma")
public class CmaController {

    @Autowired
    CmaService cmaService;

    @Autowired
    private AuthTokensGenerator authTokensGenerator;

    @Autowired
    private CmaCommentService cmaCommentService;

    // CMA 상품 리스트 저장
    @ApiOperation("*절대 호출금지 크롤링 DB에 CMA상품 정보 저장")
    @GetMapping("/save")
    public ResponseEntity<Map<String,Object>> saveCmaProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        HttpStatus status;

        try{
            cmaService.saveCmaProducts();
            status = HttpStatus.OK;

            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }

    // CMA 상품 리스트 전체 조회
    @ApiOperation(value = "DB에 저장된 모든 CMA상품 정보 조회")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCmaProducts() throws InterruptedException {

        Map<String, Object> resultMap = new HashMap<>();

        HttpStatus status;

        try {
            List<Map<String, Object>> productsList = cmaService.getCmaProductsAsMap();


            for (Map<String, Object> product : productsList) {
                if (product.containsKey("memo")) {
                    JSONObject jsonObject = new JSONObject(product);

                    String memo = jsonObject.getString("memo");

                    String[] productTypes = {"RP형", "발행어음형", "종금형"};

                    for (String productType : productTypes) {
                        String regex = productType + "\\n(\\d+(\\.\\d{1,2})?)%";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(memo);

                        if (matcher.find()) {
                            jsonObject.put(productType, matcher.group(1));
                        }
                    }

                    product.putAll(jsonObject.toMap());
                }
            }

            status = HttpStatus.OK;
            resultMap.put("products", productsList);
            resultMap.put("message", "success");

        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }

        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    // CMA 상품 상세 조회
    @ApiOperation(value = "id일치하는 CMA상품 상세정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCmaProductById(@PathVariable("id") Long productId) {

        Map<String, Object> resultMap = new HashMap<>();

        HttpStatus status;

        try {
            Map<String, Object> product = cmaService.getCmaProductByIdAsMap(productId);

            if (product.containsKey("memo")) {
                JSONObject jsonObject = new JSONObject(product);

                String memo = jsonObject.getString("memo");

                String[] productTypes = {"RP형", "발행어음형", "종금형"};

                for (String productType : productTypes) {
                    String regex = productType + "\\n(\\d+(\\.\\d{1,2})?)%";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(memo);

                    if (matcher.find()) {
                        jsonObject.put(productType, matcher.group(1));
                    }
                }
                product.putAll(jsonObject.toMap());
            }

            // 코멘트 가져오기 추가
            List<CmaComment> comments = cmaCommentService.getCmaCommentsByCmaId(productId);

            status = HttpStatus.OK;
            resultMap.put("product", product);
            resultMap.put("comments", comments);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "CMA 상품을 '찜'으로 등록")
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> saveLikedCma(@ApiParam(value = "memberId와, cmaId가 잘못되면 안들어갑니다.")
                                                            @RequestBody LikedCma likedCma,
                                                            @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                            @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            likedCma.setMemberId(memberId);

            cmaService.saveLikedCma(likedCma);
            status = HttpStatus.OK;
            resultMap.put("likedCma", likedCma);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "CMA 상품 댓글 작성")
    @PostMapping("/{cmaId}/comment") // 수정된 부분
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable Long cmaId, // 수정된 부분
                                                             @RequestBody CmaComment cmaComment,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            cmaComment.setCmaId(cmaId); // 수정된 부분
            cmaComment.setMemberId(memberId);

            cmaCommentService.createCmaComment(cmaComment);
            status = HttpStatus.CREATED;
            resultMap.put("cmaComment", cmaComment);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "CMA 상품 댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            cmaCommentService.deleteCmaComment(commentId, memberId);
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
    @ApiOperation(value = "CMA 상품 댓글 수정")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Long commentId,
                                                             @RequestBody CmaComment updateCmaComment,
                                                             @ApiParam(value = "Bearer ${jwt token} 형식으로 전송")
                                                             @RequestHeader("Authorization") String jwt) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            jwt = jwt.replace("Bearer ", "");
            Long memberId = authTokensGenerator.extractMemberId(jwt);

            cmaCommentService.updateCmaComment(commentId, memberId, updateCmaComment);
            status = HttpStatus.OK;
            resultMap.put("message", "success");
            resultMap.put("updateCmaComment", updateCmaComment);
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


