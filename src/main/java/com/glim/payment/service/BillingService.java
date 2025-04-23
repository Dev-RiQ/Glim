package com.glim.payment.service;

import com.glim.payment.domain.Billing;
import com.glim.payment.dto.response.BillingResponse;
import com.glim.payment.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
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

    public BillingResponse registerBillingKey(String impUid, String customerUid) {
        String token = getToken();

        Map<String, Object> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/subscribe/customers/" + customerUid)
                        .queryParam("imp_uid", impUid)
                        .build())
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> billingData = (Map<String, Object>) response.get("response");

        Billing billing = Billing.builder()
                .customerUid((String) billingData.get("customer_uid"))
                .billingKey((String) billingData.get("customer_uid"))
                .cardName((String) billingData.get("card_name"))
                .cardNumber((String) billingData.get("card_number"))
                .build();

        billingRepository.save(billing);

        return new BillingResponse(
                billing.getCustomerUid(),
                billing.getBillingKey(),
                billing.getCardName()
        );
    }

    public void chargeBilling(String customerUid, int amount, String name) {
        String token = getToken();

        Map<String, Object> body = Map.of(
                "customer_uid", customerUid,
                "merchant_uid", "bill_" + System.currentTimeMillis(),
                "amount", amount,
                "name", name
        );

        webClient.post()
                .uri("/subscribe/payments/again")
                .headers(h -> h.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
