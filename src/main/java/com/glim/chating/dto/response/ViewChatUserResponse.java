package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatRoom;
import com.glim.chating.domain.ChatUser;
import com.glim.chating.domain.ChatUserValid;
import com.glim.common.utils.DateTimeUtil;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ViewChatUserResponse {
    private Long id;
    private Long roomId;
    private Long userId;
    private Long readMsgId;
    private ChatUserValid valid;

    public ViewChatUserResponse(ChatUser chatUser) {
        this.id = chatUser.getId();
        this.roomId = chatUser.getRoomId();
        this.userId = chatUser.getUserId();
        this.readMsgId = chatUser.getReadMsgId();
        this.valid = chatUser.getValid();
    }
}
