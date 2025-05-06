package com.glim.payment.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.payment.domain.Billing;
import com.glim.payment.dto.response.BillingResponse;
import com.glim.payment.repository.BillingRepository;
import com.glim.payment.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * BillingController는 정기결제 관련 HTTP 요청을 처리하는 REST 컨트롤러입니다.
 */
@RestController // 해당 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냄
@RequestMapping("/billing") // 컨트롤러의 기본 URL 경로를 /billing으로 설정
@RequiredArgsConstructor // final 필드에 대해 생성자를 자동 생성 (BillingService 주입에 사용)
public class BillingController {

    private final BillingService billingService; // 결제 관련 비즈니스 로직을 처리하는 서비스

    /**
     * 정기결제용 빌링키 등록 요청을 처리하는 메서드입니다.
     * @param impUid - 포트원 결제 고유 ID (결제 승인 후 반환되는 값)
     * @param customerUid - 사용자 식별을 위한 고유 ID (정기결제를 위해 카드와 매핑됨) userName
     * @return BillingResponse - 등록 결과를 담은 응답 객체
     */
    @PostMapping("/register") // POST /billing/register 요청 처리
    public StatusResponseDTO registerBilling(
            @RequestParam String impUid, // 쿼리 파라미터로 impUid 받기
            @RequestParam String customerUid,// 쿼리 파라미터로 customerUid 받기
            @RequestParam int amount, @RequestParam String name) {
        billingService.registerBillingKey(impUid, customerUid); // 빌링키 등록
//      billingService.chargeBilling(customerUid, amount, name); // 결제하기
        return StatusResponseDTO.ok("정기 결제 등록"); // HTTP 200 OK 응답과 함께 결과 반환
    }

    @PostMapping("/cancel")
    public StatusResponseDTO cancelBilling(@RequestParam String customerUid) {
        billingService.deactivateBillingKey(customerUid);
        return StatusResponseDTO.ok("정기 결제 취소");
    }

    @GetMapping("/info")
    public StatusResponseDTO getBillingInfo(@RequestParam String customerUid) {
        Billing billing = billingService.getBillingInfo(customerUid);
        return StatusResponseDTO.ok(billing);
    }

}
