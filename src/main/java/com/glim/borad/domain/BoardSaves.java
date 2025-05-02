package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_saves")
@ToString()
public class BoardSaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_save_id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public BoardSaves(Long userId, Long boardId, LocalDateTime createdAt) {
        this.userId = userId;
        this.boardId = boardId;
        this.createdAt = createdAt;
    }

}
