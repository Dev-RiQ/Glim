package com.glim.payment.controller;

import com.glim.payment.dto.response.PaymentResponse;
import com.glim.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody Map<String, Object> payload) {
        String impUid = (String) payload.get("imp_uid");
        PaymentResponse responseDto = paymentService.verifyPayment(impUid);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping({"", "/pay"})
    public String paymentPage() {
        return "payment"; // templates/payment/payment.html 을 의미
    }
}
