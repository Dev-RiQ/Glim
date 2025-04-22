package com.glim.pay.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pay_rate")
@ToString()
public class PayRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String rate;
    @Column(nullable = false)
    private Integer price;

}
