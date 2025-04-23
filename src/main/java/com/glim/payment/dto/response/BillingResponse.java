package com.glim.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillingResponse {
    private String customerUid;
    private String billingKey;
    private String cardName;
}
