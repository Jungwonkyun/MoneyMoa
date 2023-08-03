package com.d210.moneymoa.controller;


import com.d210.moneymoa.dto.Deposit;
import com.d210.moneymoa.service.DepositService;


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
@RequestMapping("/api")
public class DepositController {

    @Autowired
    DepositService depositService;


    // 예금상품 API 정보 저장
    @GetMapping("/products/deposit/save")
    public ResponseEntity<Map<String,Object>> saveDepositProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        try{
            depositService.saveDepositProducts();
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
    }

    // 예금상품 API 정보 조회
    @GetMapping("/products/deposit/list")
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

            resultMap.put("depositProducts", depositProducts);
            resultMap.put("message", "success");
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "fail");
            status = HttpStatus.BAD_REQUEST
        }
        return new ResponseEntity<>(resultMap, status);
    }

    // 예금상품 API 상세정보 조회
    @GetMapping("/products/deposit/{productCode}")
    public ResponseEntity<Map<String, Object>> getDepositProductsWithInterestDetails(@PathVariable String productCode) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            Deposit depositProduct = depositService.getDepositProductWithInterestDetails(productCode);

            // Convert spcl string to list
            String spclStr = depositProduct.getSpcl();
            depositProduct.setSpclList(Arrays.asList(spclStr.split("\n")));

            resultMap.put("depositProduct", depositProduct);
            resultMap.put("message", "success");
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "fail");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(resultMap, status);
    }
}
