package com.glim.payment.service;

import com.glim.payment.domain.Payment;
import com.glim.payment.dto.response.PaymentResponse;
import com.glim.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebClient webClient;

    private final String API_KEY = "0206370871700345";
    private final String API_SECRET = "6SGoX3ZcWftGMQ73S9uYNPuRTNWxKXDhONU3dkZbbqhL4u3Sae3OCUkv4CEdx65wY4dgvncin5VDnImK";

    private String getToken() {
        Map<String, String> body = Map.of(
                "imp_key", API_KEY,
                "imp_secret", API_SECRET
        );

        Map response = webClient.post()
                .uri("/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> tokenInfo = (Map<String, Object>) response.get("response");
        return (String) tokenInfo.get("access_token");
    }

    public PaymentResponse verifyPayment(String impUid) {
        String token = getToken();

        Map<String, Object> response = webClient.get()
                .uri("/payments/" + impUid)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> paymentData = (Map<String, Object>) response.get("response");

        Payment payment = Payment.builder()
                .impUid((String) paymentData.get("imp_uid"))
                .merchantUid((String) paymentData.get("merchant_uid"))
                .buyerName((String) paymentData.get("buyer_name"))
                .amount((int) paymentData.get("amount"))
                .status((String) paymentData.get("status"))
                .build();

        paymentRepository.save(payment);

        return new PaymentResponse(
                payment.getImpUid(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getBuyerName()
        );
    }
}
