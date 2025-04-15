package com.glim.chating.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
@ToString()
public class ChatMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msg_id")
    private Long id;
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "user_id")
    private Long userId;
    private String content;
    @Column(name = "reply_msg_id")
    private Long replyMsgId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public ChatMsg(Long roomId, Long userId, String content, Long replyMsgId) {
        this.roomId = roomId;
        this.userId = userId;
        this.content = content;
        this.replyMsgId = replyMsgId;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String content) {
        this.content = content;
    }
}
