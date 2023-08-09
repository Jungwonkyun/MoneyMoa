package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Deposit;
import com.d210.moneymoa.dto.LikedDeposit;
import com.d210.moneymoa.repository.DepositRepository;
import com.d210.moneymoa.repository.LikedDepositRepository;
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
public class DepositServiceImpl implements DepositService {

    @Autowired
    DepositRepository depositRepository;

    @Transactional
    public void saveDepositProducts() throws InterruptedException {


        String key = "07c72ea0fe54de8626b96806cd3a0c7d";
        List<Deposit> depositProducts = new ArrayList<>();

        try {
            String apiURL = "https://finlife.fss.or.kr/finlifeapi/depositProductsSearch.json?auth=" + key + "&topFinGrpNo=020000&pageNo=1";
            URL url = new URL(apiURL);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject resultJson = (JSONObject) jsonObject.get("result");
            JSONArray baseList = (JSONArray) resultJson.get("baseList");

            for (Object productObj : baseList) {
                JSONObject productJson = (JSONObject) productObj;
                Deposit depositDto = new Deposit();
                depositDto.setProductCode((String) productJson.get("fin_prdt_cd"));
                depositDto.setBankCode((String) productJson.get("fin_co_no"));
                depositDto.setBankName((String) productJson.get("kor_co_nm"));
                depositDto.setProductName((String) productJson.get("fin_prdt_nm"));
                depositDto.setInterest((String) productJson.get("mtrt_int"));
                depositDto.setSpcl((String) productJson.get("spcl_cnd"));
                depositDto.setJoinMember((String) productJson.get("join_member"));
                depositDto.setEtcNote((String) productJson.get("etc_note"));
                depositDto.setMaxLimit((Long) productJson.get("max_limit"));

                depositProducts.add(depositDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (Deposit depositDto : depositProducts) {
                Deposit deposit = Deposit.builder()
                        .productCode(depositDto.getProductCode())
                        .bankCode(depositDto.getBankCode())
                        .bankName(depositDto.getBankName())
                        .productName(depositDto.getProductName())
                        .interest(depositDto.getInterest())
                        .spcl(depositDto.getSpcl())
                        .joinMember(depositDto.getJoinMember())
                        .etcNote(depositDto.getEtcNote())
                        .maxLimit(depositDto.getMaxLimit())
                        .build();

                depositRepository.save(deposit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Deposit> getAllDepositProducts() throws InterruptedException {
        return depositRepository.findAll();
    }

    @Override
    public Deposit getDepositProductWithInterestDetails(String productCode) {
        return depositRepository.findByProductCode(productCode).orElse(null);
    }

    @Autowired
    private LikedDepositRepository likedDepositRepository;

    @Override
    public void saveLikedDeposit(LikedDeposit likedDeposit) {
        likedDepositRepository.save(likedDeposit);
    }
}
