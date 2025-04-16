package com.glim.chating.dto.response;

import com.glim.chating.domain.ChatMsg;
import com.glim.chating.domain.ChatRoom;
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
    private Long roomId;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private LocalDateTime createdAt;

    public ViewChatMsgResponse(ChatMsg chatMsg) {
        this.msgId = chatMsg.getMsgId();
        this.roomId = chatMsg.getRoomId();
        this.userId = chatMsg.getUserId();
        this.content = chatMsg.getContent();
        this.replyMsgId = chatMsg.getReplyMsgId();
        this.createdAt = chatMsg.getCreatedAt();
    }
}
