package com.glim.payment.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "billing")
@ToString()
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerUid;
    private String billingKey;
    private String cardName;
    private String cardNumber;
    private boolean isActive;
    private LocalDate billingStartDate;

    @Builder
    public Billing(String customerUid, String billingKey, String cardName, String cardNumber, LocalDate billingStartDate) {
        this.customerUid = customerUid;
        this.billingKey = billingKey;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.isActive = true;
        this.billingStartDate = billingStartDate;
    }

}
