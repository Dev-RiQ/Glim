package com.glim.borad.domain;

import com.glim.borad.dto.request.UpdateCommentsRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_comments")
@ToString()
public class BoardComments {
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
    private LocalDateTime createdAt;
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;
    @Column(name = "reply_comment_id", nullable = false)
    private Long replyCommentId;

    @Builder
    public BoardComments(Long boardId, Long userId, String content, Long replyId) {
        this.boardId = boardId;
        this.userId = userId;
        this.content = content;
        this.likes = 0;
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.replyCommentId = replyId;
    }

    public void update(UpdateCommentsRequest request) {
        this.content = request.getContent();
        this.updateAt = LocalDateTime.now();
    }
}
