package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_likes")
@ToString()
public class CommentLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id", nullable = false)
    private Long id;
    @Column(name = "comment_id", nullable = false)
    private Long commentId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public CommentLikes(Long commentId, Long userId, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.createdAt = createdAt;
    }
}
