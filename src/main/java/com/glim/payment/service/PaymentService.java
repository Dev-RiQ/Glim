package com.glim.payment.service;

import com.glim.payment.domain.Payment; // 결제 엔티티 클래스
import com.glim.payment.dto.response.PaymentResponse; // 결제 응답 DTO
import com.glim.payment.repository.PaymentRepository; // 결제 정보를 저장하는 JPA 레포지토리
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service // 스프링 서비스 컴포넌트로 등록
@RequiredArgsConstructor // 생성자 자동 생성 (final 필드 주입)
public class PaymentService {

    private final PaymentRepository paymentRepository; // 결제 정보를 DB에 저장
    private final WebClient webClient; // 외부 API 통신을 위한 WebClient (포트원 서버와 통신)

    // 포트원 REST API 인증용 키/시크릿 (테스트용)
    private final String API_KEY = "0206370871700345";
    private final String API_SECRET = "6SGoX3ZcWftGMQ73S9uYNPuRTNWxKXDhONU3dkZbbqhL4u3Sae3OCUkv4CEdx65wY4dgvncin5VDnImK";

    /**
     * 포트원 API로부터 인증 토큰(access_token)을 발급받는 메서드
     * @return 발급된 access_token 문자열
     */
    private String getToken() {
        // 인증 요청 바디 구성
        Map<String, String> body = Map.of(
                "imp_key", API_KEY,
                "imp_secret", API_SECRET
        );

        // POST /users/getToken 요청 → 토큰 발급
        Map response = webClient.post()
                .uri("/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // 동기 방식으로 처리

        // 응답에서 access_token 추출
        Map<String, Object> tokenInfo = (Map<String, Object>) response.get("response");
        return (String) tokenInfo.get("access_token");
    }

    /**
     * impUid를 통해 결제 정보를 포트원에서 조회하고 DB에 저장한 뒤 클라이언트에 반환
     * @param impUid 포트원 결제 고유 ID
     * @return PaymentResponse 결제 상태, 금액 등 정보를 담은 DTO
     */
    public PaymentResponse verifyPayment(String impUid) {
        String token = getToken(); // 인증 토큰 발급

        // GET /payments/{imp_uid} 요청 → 결제 상세 정보 조회
        Map<String, Object> response = webClient.get()
                .uri("/payments/" + impUid)
                .headers(h -> h.setBearerAuth(token)) // Bearer 인증
                .retrieve()
                .bodyToMono(Map.class)
                .block(); // 동기 처리

        // 응답에서 결제 정보 추출
        Map<String, Object> paymentData = (Map<String, Object>) response.get("response");

        // 엔티티 생성 후 DB 저장
        Payment payment = Payment.builder()
                .impUid((String) paymentData.get("imp_uid")) // 결제 고유 ID
                .merchantUid((String) paymentData.get("merchant_uid")) // 주문 고유 ID
                .buyerName((String) paymentData.get("buyer_name")) // 구매자 이름
                .amount((int) paymentData.get("amount")) // 결제 금액
                .status((String) paymentData.get("status")) // 결제 상태
                .build();

        paymentRepository.save(payment); // DB 저장

        // 응답용 DTO 생성 후 반환
        return new PaymentResponse(
                payment.getImpUid(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getBuyerName()
        );
    }
}
