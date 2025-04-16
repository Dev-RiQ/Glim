package com.glim.common.kafka.dto;

import com.glim.chating.domain.ChatMsg;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private String id;
    private Long msgId;
    private Long roomId;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private LocalDateTime createdAt;

    public Message(ChatMsg message) {
        this.id = message.getId();
        this.msgId = message.getMsgId();
        this.roomId = message.getRoomId();
        this.userId = message.getUserId();
        this.content = message.getContent();
        this.replyMsgId = message.getReplyMsgId();
        this.createdAt = message.getCreatedAt();
    }
}
