package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Saving;
import com.d210.moneymoa.repository.SavingRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class SavingServiceImpl implements SavingService {

    @Autowired
    SavingRepository savingRepository;

    @Transactional
    public void saveSavingProducts() throws InterruptedException {

        String key = "07c72ea0fe54de8626b96806cd3a0c7d";
        List<Saving> savingProducts = new ArrayList<>();

        try {
            String apiURL = "https://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json?auth=" + key + "&topFinGrpNo=020000&pageNo=1";
            URL url = new URL(apiURL);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject resultJson = (JSONObject) jsonObject.get("result");
            JSONArray baseList = (JSONArray) resultJson.get("baseList");

            for (Object productObj : baseList) {
                JSONObject productJson = (JSONObject) productObj;
                Saving savingDto = new Saving();
                savingDto.setProductCode((String) productJson.get("fin_prdt_cd"));
                savingDto.setBankCode((String) productJson.get("fin_co_no"));
                savingDto.setBankName((String) productJson.get("kor_co_nm"));
                savingDto.setProductName((String) productJson.get("fin_prdt_nm"));
                savingDto.setInterest((String) productJson.get("mtrt_int"));
                savingDto.setSpcl((String) productJson.get("spcl_cnd"));
                savingDto.setJoinMember((String) productJson.get("join_member"));
                savingDto.setEtcNote((String) productJson.get("etc_note"));
                savingDto.setMaxLimit((Long) productJson.get("max_limit"));

                savingProducts.add(savingDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            for (Saving savingDto : savingProducts) {
                Saving saving = Saving.builder()
                        .productCode(savingDto.getProductCode())
                        .bankCode(savingDto.getBankCode())
                        .bankName(savingDto.getBankName())
                        .productName(savingDto.getProductName())
                        .interest(savingDto.getInterest())
                        .spcl(savingDto.getSpcl())
                        .joinMember(savingDto.getJoinMember())
                        .etcNote(savingDto.getEtcNote())
                        .maxLimit(savingDto.getMaxLimit())
                        .build();

                savingRepository.save(saving);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Saving> getAllSavingProducts() throws InterruptedException {
        return savingRepository.findAll();
    }

    @Override
    public Saving getSavingProductWithInterestDetails(String productCode) {
        return savingRepository.findByProductCode(productCode).orElse(null);
    }
}
