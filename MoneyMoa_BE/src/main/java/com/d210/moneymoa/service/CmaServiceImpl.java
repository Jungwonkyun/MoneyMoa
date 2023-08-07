package com.d210.moneymoa.service;

import com.d210.moneymoa.dto.Cma;
import com.d210.moneymoa.dto.LikedCma;
import com.d210.moneymoa.dto.LikedSaving;
import com.d210.moneymoa.repository.CmaRepository;

import com.d210.moneymoa.repository.LikedCmaRepository;
import com.d210.moneymoa.repository.LikedSavingRepository;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CmaServiceImpl implements CmaService {

    @Autowired
    CmaRepository cmaRepository;

    @Transactional
    public void saveCmaProducts() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\SSAFY\\Desktop\\chromedriver_win32\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        List<Cma> CmaProductList = new ArrayList<>();
        driver.get("https://new-m.pay.naver.com/savings/list/cma");

        // 페이지 끝에 도달할 때까지 무한 루프
        while (true) {

            Thread.sleep(5);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                List<WebElement> productElements = driver.findElements(By.cssSelector("strong.ProductInfo_title__X0VsD"));
                List<WebElement> bankElements = driver.findElements(By.cssSelector("p.ProductInfo_bank-name__uz2ec"));
                List<WebElement> highInterestElements = driver.findElements(By.cssSelector("b.ProductInfo_number__Thioq"));
                List<WebElement> detailLinkElements = driver.findElements(By.cssSelector("a.ProductList_link__Wj80i"));

                List<String> cmaNames = new ArrayList<>();
                List<String> stockNames = new ArrayList<>();
                List<String> maxRates = new ArrayList<>();

                for (int i = 0; i < productElements.size(); i++) {
                    String cmaName = productElements.get(i).getText();
                    String stockName = bankElements.get(i).getText();
                    String maxRate = highInterestElements.get(i).getText();

                    cmaNames.add(cmaName);
                    stockNames.add(stockName);
                    maxRates.add(maxRate);
                }

                // 초기화된 리스트 생성
                List<WebElement> memoElements = new ArrayList<>();
                List<String> memoList = new ArrayList<>();


                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("strong.ProductInfo_title__X0VsD")));

                // 링크의 수만큼 반복
                int linkSize = detailLinkElements.size();
                for (int i = 0; i < linkSize; i++) {
                    try {
                        // 링크를 다시 찾기 (페이지가 새로고침되므로)
                        List<WebElement> updatedLinkElements = driver.findElements(By.cssSelector("a.ProductList_link__Wj80i"));

                        // 웹 요소를 직접 클릭하기 위해 JavaScript를 사용
                        WebElement linkToClick = updatedLinkElements.get(i);
                        JavascriptExecutor executor = (JavascriptExecutor) driver;
                        executor.executeScript("arguments[0].click();", linkToClick);

                        // 새로운 페이지에서 원하는 요소를 로드할 시간을 기다림
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.TextList_item__xAWO8")));

                        // 새로운 페이지에서 원하는 정보를 가져옴
                        WebElement memo = driver.findElement(By.cssSelector("div#CMA_EARNING_RATE.TabPanel_article__raBfm > dl.TextList_article__RszLj"));

                        // 가져온 정보를 리스트에 추가
                        memoElements.add(memo);
                        String memos = memoElements.get(i).getText();
                        memoList.add(memos);

                        // 돌아가기
                        driver.navigate().back();

                        // 이전 페이지에서 원하는 요소를 로드할 시간을 기다림
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.ProductList_link__Wj80i")));
                    } catch (NoSuchElementException e) {
                        System.out.println("Element not found: " + e.getMessage());
                        break; // 리스트를 더 이상 처리하지 않습니다.
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                        break; // 다른 예외가 발생한 경우 리스트를 더 이상 처리하지 않습니다.
                    }
                }


                for (int i = 0; i < cmaNames.size(); i++) {
                    String cmaName = cmaNames.get(i);
                    String stockName = stockNames.get(i);
                    String maxRate = maxRates.get(i);
                    String memo = memoList.get(i);
                    Cma cmaProduct = Cma.builder()
                            .cmaName(cmaName)
                            .stockName(stockName)
                            .maxRate(maxRate)
                            .memo(memo)
                            .build();

                    cmaRepository.save(cmaProduct);

                }

            }catch (Exception e){
                e.printStackTrace();
            }

            // 다음 페이지 버튼을 찾습니다.
            WebElement nextPageButton;
            try {
                nextPageButton = driver.findElement(By.cssSelector(".Pagination_button__9HM1m.Pagination_next__DJxZI"));
            } catch (NoSuchElementException e) {
                break; // 다음 페이지 버튼이 없으면 무한 루프를 중단합니다.
            }

            // 다음 페이지 버튼이 비활성화되었으면 루프를 종료합니다.
            if (nextPageButton.getAttribute("class").contains("Pagination_disabled__vXekh")) {
                break;
            }

            // 페이지가 존재하면 다음 페이지로 이동합니다.
            // WebDriverWait를 사용하여 버튼이 클릭 가능할 때까지 기다립니다.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(nextPageButton));

            // 버튼이 화면 중앙에 오도록 스크롤 조작
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", nextPageButton);

            nextPageButton.click();

            // 다음 페이지의 내용이 로드될 시간을 기다립니다.
            Thread.sleep(3000);
        }


        driver.quit();
    }

    @Override
    public List<Cma> getCmaProducts() throws InterruptedException {
        return cmaRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> getCmaProductsAsMap() throws InterruptedException {
        List<Cma> cmaList = getCmaProducts();
        List<Map<String, Object>> productsList = cmaList.stream().map(cma -> cmaToMap(cma)).collect(Collectors.toList());
        return productsList;
    }

    @Override
    public Map<String, Object> getCmaProductByIdAsMap(Long productId) throws InterruptedException {
        Cma product = cmaRepository.findById(productId).orElse(null);
        if (product != null) {
            Map<String, Object> productAsMap = cmaToMap(product);
            return productAsMap;
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    private Map<String, Object> cmaToMap(Cma cma) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", cma.getId());
        map.put("cmaName", cma.getCmaName());
        map.put("stockName", cma.getStockName());
        map.put("maxRate", cma.getMaxRate());
        map.put("memo", cma.getMemo());
        return map;
    }

    @Autowired
    private LikedCmaRepository likedCmaRepository;

    @Override
    public void saveLikedCma(LikedCma likedCma) {
        likedCmaRepository.save(likedCma);
    }
}


