package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_likes")
@ToString()
public class boardLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_like_id", nullable = false)
    private Long id;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at", nullable = false)
    private String createdAt;

}
