package com.glim.pay.controller;

import com.glim.pay.util.PortOneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${portone.imp-key}")
    private String portoneImpKey; // 가맹점 식별코드 주입

    @Autowired
    private PortOneUtil portOneUtil; // 포트원 유틸리티 주입

    // 결제 페이지 렌더링
    @GetMapping("/page")
    public String paymentPage(Model model) {
        model.addAttribute("portoneImpKey", portoneImpKey); // Thymeleaf로 imp-key 전달
        return "board/payment"; // payment.html 템플릿 렌더링
    }

    // 결제 검증 API
    @GetMapping("/verify/{impUid}")
    @ResponseBody
    public ResponseEntity<?> verifyPayment(@PathVariable String impUid) {
        try {
            // 액세스 토큰 발급
            String accessToken = portOneUtil.getAccessToken();
            System.out.println("accessToken: " + accessToken);
            // 결제 정보 조회
            Map<String, Object> paymentInfo = portOneUtil.getPaymentInfo(impUid, accessToken);

            // 응답에서 결제 정보 추출
            Map<String, Object> response = (Map<String, Object>) paymentInfo.get("response");
            if (response == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "결제 정보 없음"));
            }

            // 결제 금액 및 상태 확인
            Integer amount = (Integer) response.get("amount");
            String status = (String) response.get("status");

            if ("paid".equals(status) && amount != null) {
                // TODO: DB에 결제 정보 저장 (예: Payment 엔티티)
                return ResponseEntity.ok(Map.of("message", "결제 검증 성공", "amount", amount));
            } else {
                // 결제 상태가 비정상인 경우
                return ResponseEntity.badRequest().body(Map.of("message", "결제 상태 비정상"));
            }
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(500).body(Map.of("message", "검증 실패: " + e.getMessage()));
        }
    }

    // 모바일 결제 리다이렉션 처리
    @GetMapping("/redirect")
    public String handleRedirect(@RequestParam Map<String, String> params) {
        // 모바일 결제 완료 후 리다이렉션 URL로 전달된 파라미터 처리
        return "redirect:/payment-result?imp_uid=" + params.get("imp_uid") + "&merchant_uid=" + params.get("merchant_uid");
    }
}