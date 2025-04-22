package com.glim.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowRequest {
    private Long followerId; // 팔로우를 거는사람 ( 해당 id를 가지고 있는 사람 following +1 )
    private Long followingId; // 팔로잉을 당하는사람 ( 해당 id를 가지고 있는 사람 follwer +1 )
}
