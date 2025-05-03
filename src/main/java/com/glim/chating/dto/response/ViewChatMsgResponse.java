package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
import com.glim.common.utils.DateTimeUtil;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
public class ViewChatMsgResponse {
    private Long msgId;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private String createdAt;

    public ViewChatMsgResponse(ChatMsg chatMsg) {
        this.msgId = chatMsg.getMsgId();
        this.userId = chatMsg.getUserId();
        this.content = chatMsg.getContent();
        this.replyMsgId = chatMsg.getReplyMsgId();
        this.createdAt = DateTimeUtil.getTime(chatMsg.getCreatedAt());
    }
}
