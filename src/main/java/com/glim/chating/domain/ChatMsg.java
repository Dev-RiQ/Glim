package com.glim.chating.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Document(collection = "chat_msgs")
@ToString()
public class ChatMsg {
    @Column(name = "_id")
    private String id;
    private Long msgId;
    private Long roomId;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private LocalDateTime createdAt;

    @Builder
    public ChatMsg(Long roomId, Long userId, String content, Long replyMsgId) {
        this.roomId = roomId;
        this.userId = userId;
        this.content = content;
        this.replyMsgId = replyMsgId;
        this.createdAt = LocalDateTime.now();
    }
}
