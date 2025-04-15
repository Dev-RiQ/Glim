package com.glim.chating.dto.request;

import com.glim.chating.domain.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddChatRoomRequest {
    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .now(LocalDateTime.now())
                .build();
    }
}
