package com.glim.pay.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PortOneUtil {

    // 포트원 API 인증 정보 주입 (application.yml에서 가져옴)
    @Value("${portone.api-key}")
    private String apiKey;

    @Value("${portone.api-secret}")
    private String apiSecret;

    // JSON 파싱용 ObjectMapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 포트원 API 액세스 토큰 발급
    public String getAccessToken() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.iamport.kr/users/getToken");

        // 요청 본문: API 키와 시크릿
        Map<String, String> request = new HashMap<>();
        request.put("imp_key", apiKey);
        request.put("imp_secret", apiSecret);

        // JSON으로 변환
        String json = objectMapper.writeValueAsString(request);
        httpPost.setEntity(new StringEntity(json, "UTF-8"));
        httpPost.setHeader("Content-Type", "application/json");

        // API 호출 및 응답 처리
        try (CloseableHttpResponse response = client.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
            return (String) ((Map) result.get("response")).get("access_token");
        }
    }

    // 결제 정보 조회
    public Map<String, Object> getPaymentInfo(String impUid, String accessToken) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.iamport.kr/payments/" + impUid);
        httpGet.setHeader("Authorization", accessToken); // 인증 헤더 추가

        // API 호출 및 응답 처리
        try (CloseableHttpResponse response = client.execute(httpGet)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            return objectMapper.readValue(responseBody, Map.class);
        }
    }
}