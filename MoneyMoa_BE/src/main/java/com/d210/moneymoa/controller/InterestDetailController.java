package com.d210.moneymoa.controller;

import com.d210.moneymoa.service.InterestDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/interestdetail")
public class InterestDetailController {

    @Autowired
    InterestDetailService interestDetailService;

    @ApiOperation(value = "예금상품 InterestDetail DB저장 *예금정보에서 조회가능할시 호출 금지")
    @GetMapping("/deposit")
    public ResponseEntity<Map<String,Object>> saveDepositInterestDetailProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        HttpStatus status;

        try{
            interestDetailService.saveDepositInterestDetailProducts();
            status = HttpStatus.OK;
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }

    @ApiOperation(value = "적금상품 InterestDetail DB저장 *예금정보에서 조회가능할시 호출 금지")
    @GetMapping("/saving")
    public ResponseEntity<Map<String,Object>> saveSavingInterestDetailProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        HttpStatus status;

        try{
            interestDetailService.saveSavingInterestDetailProducts();
            status = HttpStatus.OK;
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }
}
