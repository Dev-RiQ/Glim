package com.glim.pay.service;

import com.glim.pay.config.PayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayService {
    private final PayConfig portOneConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> verifyPayment(String impUid, String merchantUid) throws Exception {
        // 1. 포트원 토큰 발급
        String token = getPortOneToken();

        // 2. 결제 정보 조회
        String apiUrl = "https://api.iamport.kr/payments/" + impUid;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");

        // 3. 결제 검증
        if (responseData != null && "paid".equals(responseData.get("status"))) {
            int paidAmount = (int) responseData.get("amount");
            String orderMerchantUid = (String) responseData.get("merchant_uid");

            // 주문 금액과 결제 금액 비교 (DB에서 주문 정보 조회 필요)
            if (orderMerchantUid.equals(merchantUid) && paidAmount == 15000) { // 예: 15000원
                return Map.of("message", "결제 성공", "data", responseData);
            } else {
                throw new IllegalStateException("결제 금액 또는 주문 번호 불일치");
            }
        } else {
            throw new IllegalStateException("결제 상태 비정상: " + responseData.get("status"));
        }
    }

    private String getPortOneToken() throws Exception {
        String apiUrl = "https://api.iamport.kr/users/getToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("imp_key", portOneConfig.getApiKey(), "imp_secret", portOneConfig.getApiSecret());

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        Map<String, String> responseData = (Map<String, String>) responseBody.get("response");

        return responseData.get("access_token");
    }
}
