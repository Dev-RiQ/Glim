package com.glim.payment.dto.response;

import lombok.AllArgsConstructor; // 모든 필드를 포함한 생성자를 자동 생성
import lombok.Data; // getter, setter, toString, equals, hashCode 등을 자동 생성

/**
 * 정기결제 카드 등록(빌링키 발급) 후 응답 데이터를 담는 DTO 클래스입니다.
 */
@Data
@AllArgsConstructor
public class BillingResponse {

    // 고객 고유 식별자 (정기결제에 사용됨)
    private String customerUid;

    // 포트원에서 발급한 정기결제용 billingKey (customerUid와 동일한 경우도 있음)
    private String billingKey;

    // 카드 이름 (예: "국민카드", "삼성카드")
    private String cardName;
}
