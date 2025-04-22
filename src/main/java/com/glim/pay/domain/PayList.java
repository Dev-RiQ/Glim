package com.glim.pay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pay_list")
@ToString()
public class PayList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "rate_id", nullable = false)
    private Integer rateId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private Refund refund;

    @Builder
    public PayList(Long userId, Integer rateId, LocalDateTime createdAt, LocalDateTime endDate, Boolean refund) {
        this.userId = userId;
        this.rateId = rateId;
        this.createdAt = createdAt;
        this.endDate = endDate;
        this.refund = refund ? Refund.REFUNDED : Refund.PAID;
    }
}
