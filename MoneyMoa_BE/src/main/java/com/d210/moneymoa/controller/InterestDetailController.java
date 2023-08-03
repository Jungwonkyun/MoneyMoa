package com.d210.moneymoa.controller;

import com.d210.moneymoa.service.InterestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InterestDetailController {

    @Autowired
    InterestDetailService interestDetailService;
    @GetMapping("/products/interestdetail/deposit")
    public ResponseEntity<Map<String,Object>> saveDepositInterestDetailProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        try{
            interestDetailService.saveDepositInterestDetailProducts();
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/products/interestdetail/saving")
    public ResponseEntity<Map<String,Object>> saveSavingInterestDetailProducts(String[] args) throws InterruptedException {

        Map<String,Object>resultMap = new HashMap<>();

        try{
            interestDetailService.saveSavingInterestDetailProducts();
            resultMap.put("messasge","success");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("messasge","fail");
        }
        return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
    }
}
