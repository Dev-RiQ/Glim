package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatRoom;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewChatMsgResponse {
    private Long id;

    public ViewChatMsgResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
    }
}
