package com.d210.moneymoa.controller;


import com.d210.moneymoa.dto.Deposit;
import com.d210.moneymoa.service.DepositService;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products/deposit")
public class DepositController {

    @Autowired
    DepositService depositService;


    // 예금상품 API 정보 저장
    @ApiOperation(value = "DB에 예금상품 API 정보 저장 *상품정보 조회가능할시 호출 금지")
    @GetMapping("/save")
    public ResponseEntity<Map<String,Object>> saveDepositProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        HttpStatus status;

        try{
            depositService.saveDepositProducts();
            status = HttpStatus.OK;
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, status);
    }

    // 예금상품 API 정보 조회
    @ApiOperation(value = "DB에 저장된 모든 예금상품 정보 조회")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getDepositProductsWithInterestDetails() {
        
        Map<String, Object> resultMap = new HashMap<>();
        
        HttpStatus status;

        try {
            List<Deposit> depositProducts = depositService.getAllDepositProducts();

            // spcl 문자열을 리스트로 변환
            for (Deposit deposit : depositProducts) {
                String spclStr = deposit.getSpcl();
                deposit.setSpclList(Arrays.asList(spclStr.split("\n")));

            }

            status = HttpStatus.OK;
            resultMap.put("depositProducts", depositProducts);
            resultMap.put("message", "success");
            
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 예금상품 API 상세정보 조회
    @ApiOperation(value = "productCode에 해당하는 예금상품 상세정보 조회")
    @GetMapping("/{productCode}")
    public ResponseEntity<Map<String, Object>> getDepositProductsWithInterestDetails(@PathVariable String productCode) {
        
        Map<String, Object> resultMap = new HashMap<>();
        
        HttpStatus status;

        try {
            Deposit depositProduct = depositService.getDepositProductWithInterestDetails(productCode);

            // Convert spcl string to list
            String spclStr = depositProduct.getSpcl();
            depositProduct.setSpclList(Arrays.asList(spclStr.split("\n")));

            status = HttpStatus.OK;
            resultMap.put("depositProduct", depositProduct);
            resultMap.put("message", "success");
            
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
            
        }
        return new ResponseEntity<>(resultMap, status);
    }
}
