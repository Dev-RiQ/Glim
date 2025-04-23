package com.glim.payment.dto.request;

import lombok.Data;

@Data
public class BillingRequest {
    private String customerUid;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
}
