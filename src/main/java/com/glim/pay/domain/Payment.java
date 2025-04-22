package com.glim.pay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
@ToString()
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String merchantUid; // 주문 번호
    private String impUid;      // 포트원 결제 고유 번호
    private Integer amount;     // 결제 금액
    private String status;      // 결제 상태 (paid, failed 등)
    private String pgProvider;  // PG사 (tosspay, kakaopay 등)
    private LocalDateTime paidAt; // 결제 완료 시간
}