package com.glim.payment.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderName;
    private int amount;
    private String buyerName;
    private String buyerEmail;
    private String buyerTel;
}
