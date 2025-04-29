package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_share")
@ToString()
public class BoardShare {
    @Id
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    public BoardShare(Long boardId, Long userId) {
        this.boardId = boardId;
        this.userId = userId;
    }
}
