package com.glim.payment.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String impUid;
    private String merchantUid;
    private String buyerName;
    private int amount;
    private String status;
}
