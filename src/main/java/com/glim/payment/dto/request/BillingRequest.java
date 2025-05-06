package com.glim.payment.dto.request;

import lombok.Data; // Lombok의 @Data는 getter/setter, toString, equals, hashCode, 기본 생성자 등을 자동 생성

/**
 * 정기결제 요청 시 클라이언트로부터 전달받는 고객 정보를 담는 DTO 클래스입니다.
 */
@Data
public class BillingRequest {

    // 고객 고유 ID (정기결제에 사용될 customer_uid)
    private String customerUid;

    // 고객 이름
    private String buyerName;

    // 고객 연락처 (전화번호)
    private String buyerTel;

    // 고객 이메일 주소
    private String buyerEmail;
}
