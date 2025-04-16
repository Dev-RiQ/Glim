package com.glim.common.kafka.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private Long id;
    private Long roomId;
    private Long userId;
    private String content;
    private Long replyMsgId;
    private LocalDateTime createdAt;
}
