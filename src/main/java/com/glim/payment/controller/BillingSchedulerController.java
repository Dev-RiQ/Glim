package com.glim.payment.controller;

import com.glim.payment.domain.Billing;
import com.glim.payment.repository.BillingRepository;
import com.glim.payment.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingSchedulerController {

    private final BillingService billingService;
    private final BillingRepository billingRepository;
    private final WebClient webClient;

    @Value("${portone.api-key}")
    private String API_KEY;

    @Value("${portone.api-secret}")
    private String API_SECRET;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void runDailyBillingByUserPaymentDay() {
        List<Billing> billingList = billingRepository.findAll();
        LocalDate today = LocalDate.now();
        int todayDay = today.getDayOfMonth();
        int lastDayOfThisMonth = today.lengthOfMonth();

        for (Billing billing : billingList) {
            LocalDate startDate = billing.getBillingStartDate();
            if (startDate == null) continue;

            int originalDay = startDate.getDayOfMonth();
            int originalLastDay = startDate.lengthOfMonth();
            boolean isOriginalEndOfMonth = (originalDay == originalLastDay);

            boolean isPaymentDay = (isOriginalEndOfMonth && todayDay == lastDayOfThisMonth)
                    || (!isOriginalEndOfMonth && todayDay == originalDay);

            if (isPaymentDay) {
                if (billing.isActive()) {
                    try {
                        billingService.chargeBilling(billing.getCustomerUid(), 9900, "월간 정기결제");
                        System.out.println("✅ 결제 성공: " + billing.getCustomerUid());
                    } catch (Exception e) {
                        System.err.println("❌ 결제 실패: " + billing.getCustomerUid());
                    }
                } else {
                    Map<String, String> body = Map.of("imp_key", API_KEY, "imp_secret", API_SECRET);
                    Map response = webClient.post()
                            .uri("/users/getToken")
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .block();
                    Map<String, Object> tokenInfo = (Map<String, Object>) response.get("response");
                    String token = (String) tokenInfo.get("access_token");
                    webClient.delete()
                            .uri("/subscribe/customers/" + billing.getCustomerUid())
                            .headers(h -> h.setBearerAuth(token))
                            .retrieve()
                            .bodyToMono(Void.class)
                            .block();
                    billingRepository.deleteByCustomerUid(billing.getCustomerUid());
                    System.out.println("🔕 구독 취소 처리 완료: " + billing.getCustomerUid());
                }
            }
        }
    }
}
