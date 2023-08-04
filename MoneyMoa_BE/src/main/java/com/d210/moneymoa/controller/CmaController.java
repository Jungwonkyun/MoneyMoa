package com.d210.moneymoa.controller;

import com.d210.moneymoa.service.CmaService;

import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/products/cma")
public class CmaController {

    @Autowired
    CmaService cmaService;

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
            resultMap.put("data", productsList);
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
            status = HttpStatus.OK;
            resultMap.put("data", product);
            resultMap.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
            resultMap.put("message", "fail");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }



}


