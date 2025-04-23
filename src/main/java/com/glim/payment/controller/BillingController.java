package com.glim.payment.controller;

import com.glim.payment.dto.response.BillingResponse;
import com.glim.payment.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/register")
    public ResponseEntity<BillingResponse> registerBilling(
            @RequestParam String impUid,
            @RequestParam String customerUid) {
        BillingResponse response = billingService.registerBillingKey(impUid, customerUid);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/charge")
    public ResponseEntity<String> chargeBilling(
            @RequestParam String customerUid,
            @RequestParam int amount,
            @RequestParam String name) {
        billingService.chargeBilling(customerUid, amount, name);
        return ResponseEntity.ok("결제 완료");
    }
}
