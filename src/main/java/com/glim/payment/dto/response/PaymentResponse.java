package com.glim.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String impUid;
    private String status;
    private int amount;
    private String buyerName;
}
