package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.common.utils.DateTimeUtil;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class ViewChatRoomResponse {
    private String updatedAt;

    public ViewChatRoomResponse(ChatRoom chatRoom) {
        this.updatedAt = DateTimeUtil.getDateTimeAgo(chatRoom.getCreatedAt());
    }
}
