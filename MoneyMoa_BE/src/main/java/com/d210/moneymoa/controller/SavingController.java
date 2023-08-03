package com.d210.moneymoa.controller;

import com.d210.moneymoa.dto.Saving;
import com.d210.moneymoa.service.SavingService;

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
public class SavingController {

    @Autowired
    SavingService savingService;


    // 적금상품 API 정보 저장
    @GetMapping("/products/saving/save")
    public ResponseEntity<Map<String,Object>> saveSavingProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        try{
            savingService.saveSavingProducts();
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
    }

    // 적금상품 API 정보 조회
    @GetMapping("/products/saving/list")
    public ResponseEntity<Map<String, Object>> getSavingProductsWithInterestDetails() {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<Saving> savingProducts = savingService.getAllSavingProducts();

            // spcl 문자열을 리스트로 변환
            for (Saving saving : savingProducts) {
                String spclStr = saving.getSpcl();
                saving.setSpclList(Arrays.asList(spclStr.split("\n")));
            }

            resultMap.put("savingProducts", savingProducts);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }


    // 적금상품 API 상세정보 조회
    @GetMapping("/products/saving/{productCode}")
    public ResponseEntity<Map<String, Object>> getSavingProductsWithInterestDetails(@PathVariable String productCode) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        try {
            Saving savingProduct = savingService.getSavingProductWithInterestDetails(productCode);

            // Convert spcl string to list
            String spclStr = savingProduct.getSpcl();
            savingProduct.setSpclList(Arrays.asList(spclStr.split("\n")));

            resultMap.put("savingProduct", savingProduct);
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




