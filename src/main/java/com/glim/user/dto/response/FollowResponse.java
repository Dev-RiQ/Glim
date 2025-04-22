package com.glim.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponse {
    private String message;
    private boolean success;
}
