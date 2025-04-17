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
    private Long roomId;
    private String img;
    private String nickname;
    private String msg;
    private String updatedAt;

    public ViewChatRoomResponse(ChatRoom chatRoom, User user) {
        this.roomId = chatRoom.getId();
        this.img = user.getImg();
        this.nickname = user.getNickname();
        this.msg = "새로운 채팅방이 생성되었습니다.";
        this.updatedAt = DateTimeUtil.getDateTimeAgo(chatRoom.getCreatedAt());
    }

    public ViewChatRoomResponse(ChatRoom chatRoom, ChatMsg chatMsg, User user) {
        this.roomId = chatRoom.getId();
        this.img = user.getImg();
        this.nickname = user.getNickname();
        this.msg = chatMsg.getContent();
        this.updatedAt = DateTimeUtil.getDateTimeAgo(chatRoom.getCreatedAt());
    }
}
