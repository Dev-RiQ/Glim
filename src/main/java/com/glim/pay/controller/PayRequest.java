package com.glim.pay.controller;

import lombok.Data;

@Data
public class PayRequest {
    private String impUid;
    private String merchantUid;
}
