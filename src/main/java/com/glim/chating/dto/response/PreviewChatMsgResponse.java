package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.common.utils.DateTimeUtil;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PreviewChatMsgResponse {
    private String content;
    private String createdAt;

    public PreviewChatMsgResponse(ChatMsg chatMsg) {
        this.content = chatMsg.getContent();
        this.createdAt = DateTimeUtil.getDateTimeAgo(chatMsg.getCreatedAt());
    }
}
