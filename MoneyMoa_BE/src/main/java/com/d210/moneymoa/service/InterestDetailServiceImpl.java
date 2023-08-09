package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.DepositInterestDetail;
import com.d210.moneymoa.dto.SavingInterestDetail;
import com.d210.moneymoa.repository.DepositInterestDetailRepository;
import com.d210.moneymoa.repository.SavingInterestDetailRepository;
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
public class InterestDetailServiceImpl implements InterestDetailService{

    @Autowired
    DepositInterestDetailRepository depositInterestDetailRepository;

    @Autowired
    SavingInterestDetailRepository savingInterestDetailRepository;
    @Transactional
    public void saveDepositInterestDetailProducts() throws InterruptedException  {

        String key = "07c72ea0fe54de8626b96806cd3a0c7d";
        List<DepositInterestDetail> depositInterestDetailProducts = new ArrayList<>();

        try {
            String apiURL = "https://finlife.fss.or.kr/finlifeapi/depositProductsSearch.json?auth=" + key + "&topFinGrpNo=020000&pageNo=1";
            URL url = new URL(apiURL);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject resultJson = (JSONObject) jsonObject.get("result");
            JSONArray optionList = (JSONArray)resultJson.get("optionList");

            for (Object productObj : optionList) {
                JSONObject productJson = (JSONObject) productObj;
                DepositInterestDetail depositInterestDetailDto = new DepositInterestDetail();
                depositInterestDetailDto.setProductCode((String) productJson.get("fin_prdt_cd"));
                depositInterestDetailDto.setPeriod(Integer.parseInt((String) productJson.get("save_trm")));
                depositInterestDetailDto.setInterestType((String) productJson.get("intr_rate_type_nm"));
                depositInterestDetailDto.setRsrvType((String) "");
                depositInterestDetailDto.setBasicRate(String.valueOf(productJson.get("intr_rate")));
                depositInterestDetailDto.setMaxRate(String.valueOf(productJson.get("intr_rate2")));

                depositInterestDetailProducts.add(depositInterestDetailDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            for (DepositInterestDetail depositInterestDetailDto : depositInterestDetailProducts) {
                DepositInterestDetail depositInterestDetail = DepositInterestDetail.builder()
                        .productCode(depositInterestDetailDto.getProductCode())
                        .period(depositInterestDetailDto.getPeriod())
                        .interestType(depositInterestDetailDto.getInterestType())
                        .rsrvType(depositInterestDetailDto.getRsrvType())
                        .basicRate(depositInterestDetailDto.getBasicRate())
                        .maxRate(depositInterestDetailDto.getMaxRate())
                        .build();

                depositInterestDetailRepository.save(depositInterestDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void saveSavingInterestDetailProducts() throws InterruptedException  {

        String key = "07c72ea0fe54de8626b96806cd3a0c7d";
        List<SavingInterestDetail> savingInterestDetailProducts = new ArrayList<>();

        try {
            String apiURL = "https://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json?auth=" + key + "&topFinGrpNo=020000&pageNo=1";
            URL url = new URL(apiURL);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject resultJson = (JSONObject) jsonObject.get("result");
            JSONArray optionList = (JSONArray)resultJson.get("optionList");

            for (Object productObj : optionList) {
                JSONObject productJson = (JSONObject) productObj;
                SavingInterestDetail savingInterestDetailDto = new SavingInterestDetail();
                savingInterestDetailDto.setProductCode((String) productJson.get("fin_prdt_cd"));
                savingInterestDetailDto.setPeriod(Integer.parseInt((String) productJson.get("save_trm")));
                savingInterestDetailDto.setInterestType((String) productJson.get("intr_rate_type_nm"));
                savingInterestDetailDto.setRsrvType((String) productJson.get("rsrv_type_nm"));
                savingInterestDetailDto.setBasicRate(String.valueOf(productJson.get("intr_rate")));
                savingInterestDetailDto.setMaxRate(String.valueOf(productJson.get("intr_rate2")));

                savingInterestDetailProducts.add(savingInterestDetailDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            for (SavingInterestDetail savingInterestDetailDto : savingInterestDetailProducts) {
                SavingInterestDetail savingInterestDetail = SavingInterestDetail.builder()
                        .productCode(savingInterestDetailDto.getProductCode())
                        .period(savingInterestDetailDto.getPeriod())
                        .interestType(savingInterestDetailDto.getInterestType())
                        .rsrvType(savingInterestDetailDto.getRsrvType())
                        .basicRate(savingInterestDetailDto.getBasicRate())
                        .maxRate(savingInterestDetailDto.getMaxRate())
                        .build();

                savingInterestDetailRepository.save(savingInterestDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
