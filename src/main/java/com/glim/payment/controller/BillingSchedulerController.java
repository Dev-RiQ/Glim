package com.glim.payment.controller;

import com.glim.payment.domain.Billing;
import com.glim.payment.repository.BillingRepository;
import com.glim.payment.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingSchedulerController {

    private final BillingService billingService;
    private final BillingRepository billingRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void runDailyBillingByUserPaymentDay() {
        List<Billing> billingList = billingRepository.findAllByIsActiveTrue();
        LocalDate today = LocalDate.now();
        int todayDay = today.getDayOfMonth();
        int lastDayOfThisMonth = today.lengthOfMonth();

        for (Billing billing : billingList) {
            LocalDate startDate = billing.getBillingStartDate();
            if (startDate == null) continue;

            int originalDay = startDate.getDayOfMonth();
            int originalLastDay = startDate.lengthOfMonth();
            boolean isOriginalEndOfMonth = (originalDay == originalLastDay);

            if ((isOriginalEndOfMonth && todayDay == lastDayOfThisMonth)
                    || (!isOriginalEndOfMonth && todayDay == originalDay)) {
                try {
                    billingService.chargeBilling(billing.getCustomerUid(), 9900, "월간 정기결제");
                    System.out.println("✅ 결제 성공: " + billing.getCustomerUid());
                } catch (Exception e) {
                    System.err.println("❌ 결제 실패: " + billing.getCustomerUid());
                }
            }
        }
    }
}
