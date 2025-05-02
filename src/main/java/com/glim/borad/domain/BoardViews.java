package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_views")
@ToString()
public class BoardViews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_view_id", nullable = false)
    private Long id;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    public BoardViews(Long boardId, Long userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}
