package com.glim.admin.dto.request;


import com.glim.admin.domain.AdvertisementStatus;

public class AddAdvertisementRequest {
    private Long id;
    private Long boardId;
    private AdvertisementStatus status;
    private String rejectionReason;
}
