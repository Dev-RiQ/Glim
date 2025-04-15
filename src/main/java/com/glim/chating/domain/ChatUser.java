package com.glim.chating.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_users")
@ToString()
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_user_id")
    private Long id;
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "read_msg_id")
    private Long readMsgId;
    @Enumerated(EnumType.STRING)
    private ChatUserValid valid;

    @Builder
    public ChatUser(Long roomId, Long userId, Long readMsgId) {
        this.roomId = roomId;
        this.userId = userId;
        this.readMsgId = readMsgId;
        this.valid = ChatUserValid.IN;
    }

    public void update(Long readMsgId) {
        this.readMsgId = readMsgId;
    }
    public void userOut() {
        this.valid = ChatUserValid.OUT;
    }
}
