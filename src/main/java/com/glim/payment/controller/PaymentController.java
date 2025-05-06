package com.glim.payment.controller;

import com.glim.payment.dto.response.PaymentResponse;
import com.glim.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // 해당 클래스가 REST API 컨트롤러임을 명시 (JSON 응답 반환)
@RequestMapping("/payment") // 이 컨트롤러는 /payment 경로로 시작하는 요청을 처리
@RequiredArgsConstructor // final로 선언된 필드에 대해 생성자를 자동 생성
public class PaymentController {

    private final PaymentService paymentService; // 결제 관련 비즈니스 로직을 담당하는 서비스 클래스

    /**
     * 클라이언트에서 결제 완료 후 전달받은 imp_uid로 결제 유효성을 검증하는 API
     * @param payload - 클라이언트로부터 전달된 JSON (imp_uid 포함)
     * @return 결제 정보를 포함한 PaymentResponse 객체
     */
    @PostMapping("/verify") // POST /payment/verify 요청 처리
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody Map<String, Object> payload) {
        String impUid = (String) payload.get("imp_uid"); // 결제 고유 ID 추출
        PaymentResponse responseDto = paymentService.verifyPayment(impUid); // 결제 정보 검증 및 응답 DTO 생성
        return ResponseEntity.ok(responseDto); // HTTP 200 OK와 함께 응답 반환
    }

    /**
     * 결제 테스트 페이지를 반환하는 GET 엔드포인트
     * @return 템플릿 파일 이름 (타임리프에서 "templates/payment/payment.html" 렌더링됨)
     */
    @GetMapping({"", "/pay"}) // GET /payment 또는 /payment/pay 요청 처리
    public String paymentPage() {
        return "payment"; // 타임리프 템플릿 파일 이름 반환 (ex: src/main/resources/templates/payment.html)
    }
}
