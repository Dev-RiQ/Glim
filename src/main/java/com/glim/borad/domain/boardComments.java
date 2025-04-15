package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_comments")
@ToString()
public class boardComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Integer likes;
    @Column(name = "created_at", nullable = false)
    private String createdAt;
    @Column(name = "update_at", nullable = false)
    private String updateAt;
    @Column(name = "reply_comment_id", nullable = false)
    private Long replyCommentId;
}
