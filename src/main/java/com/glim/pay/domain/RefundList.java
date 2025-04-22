package com.glim.pay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refund_list")
@ToString()
public class RefundList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "rate_id", nullable = false)
    private Integer rateId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
