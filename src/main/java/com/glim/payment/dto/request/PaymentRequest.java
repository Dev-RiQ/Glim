package com.glim.payment.dto.request;

import lombok.Data; // Lombok 어노테이션: getter, setter, toString, equals, hashCode, 기본 생성자 자동 생성

/**
 * 일반 결제 요청 시 필요한 정보를 담는 DTO 클래스입니다.
 */
@Data
public class PaymentRequest {

    // 주문명 (결제할 상품명 또는 서비스명)
    private String orderName;

    // 결제 금액 (단위: 원)
    private int amount;

    // 구매자 이름
    private String buyerName;

    // 구매자 이메일 주소
    private String buyerEmail;

    // 구매자 전화번호
    private String buyerTel;
}
