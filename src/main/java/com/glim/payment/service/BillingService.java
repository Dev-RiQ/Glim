package com.glim.payment.service;

import com.glim.payment.domain.Billing;
import com.glim.payment.dto.response.BillingResponse;
import com.glim.payment.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final WebClient webClient;

    @Value("${portone.api-key}")
    private String API_KEY;

    @Value("${portone.api-secret}")
    private String API_SECRET;

    private String getToken() {
        Map<String, String> body = Map.of("imp_key", API_KEY, "imp_secret", API_SECRET);
        Map response = webClient.post()
                .uri("/users/getToken")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        Map<String, Object> tokenInfo = (Map<String, Object>) response.get("response");
        return (String) tokenInfo.get("access_token");
    }

    @Transactional
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

        Billing billing = Billing.builder()
                .customerUid("user_001")
                .billingKey("user_001")
                .cardName("toss")
                .cardNumber("5684-4898-4868-4568")
                .billingStartDate(LocalDate.now())
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
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @Transactional
    public void deactivateBillingKey(String customerUid) {
        String token = getToken();
        webClient.delete()
                .uri("/subscribe/customers/" + customerUid)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        Billing billing = billingRepository.findByCustomerUid(customerUid)
                .orElseThrow(() -> new IllegalStateException("해당 customerUid 없음"));
        billingRepository.deleteByCustomerUid(customerUid);
    }
}
