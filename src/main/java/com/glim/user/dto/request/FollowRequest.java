package com.glim.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowRequest { // 팔로우 요청 시 사용하는 dto
    private Long followingId; // 팔로잉을 당하는사람 ( 해당 id를 가지고 있는 사람 follwer +1 )
}
