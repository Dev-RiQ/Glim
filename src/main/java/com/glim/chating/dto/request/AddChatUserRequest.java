package com.glim.chating.dto.request;

import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddChatUserRequest {
    private Long roomId;
    private Long userId;
    private Long readMsgId;

    public ChatUser toEntity(AddChatUserRequest addChatRoomRequest) {
        return ChatUser.builder()
                .roomId(addChatRoomRequest.getRoomId())
                .userId(addChatRoomRequest.getUserId())
                .readMsgId(addChatRoomRequest.getReadMsgId())
                .build();
    }
}
