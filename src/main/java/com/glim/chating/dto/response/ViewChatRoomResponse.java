package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import com.glim.common.utils.DateTimeUtil;
import com.glim.user.domain.User;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class ViewChatRoomResponse {

    private ViewChatUserResponse user;
    private Long roomId;
    private String msg;
    private String updatedAt;
    private Boolean hasRead;

    public ViewChatRoomResponse(ChatRoom chatRoom, ViewChatUserResponse user) {
        this.user = user;
        this.roomId = chatRoom.getId();
        this.msg = "새로운 채팅방이 생성되었습니다.";
        this.updatedAt = DateTimeUtil.getDateTimeAgo(chatRoom.getCreatedAt());
    }

    public ViewChatRoomResponse(ChatRoom chatRoom, ChatMsg chatMsg, ViewChatUserResponse user, boolean hasRead) {
        this.user = user;
        this.roomId = chatRoom.getId();
        this.msg = chatMsg != null ? chatMsg.getContent() : "새로운 채팅방이 생성되었습니다.";
        this.updatedAt = DateTimeUtil.getDateTimeAgo(chatRoom.getUpdatedAt());
        this.hasRead = hasRead;
    }
}
