package com.glim.borad.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boards")
@ToString()
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Integer views;
    @Column(nullable = false)
    private Integer likes;
    @Column(nullable = false)
    private Integer comments;
    @Column(name = "tag_user_ids", nullable = false)
    private String tagUserIds;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "bgm_id", nullable = false)
    private Long bgmId;
    @Column(name = "board_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;
    @Column(name = "view_likes", nullable = false)
    @Enumerated(EnumType.STRING)
    private Option viewLikes;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Option commentable;

    @Builder
    public Boards(Long userId, String location, String content, String tagUserIds, Long bgmId, String boardType, Boolean viewLikes, Boolean commentable) {
        this.userId = userId;
        this.location = location;
        this.content = content;
        this.tagUserIds = tagUserIds;
        this.createdAt = LocalDateTime.now();
        this.likes = 0;
        this.comments = 0;
        this.views = 0;
        this.bgmId = bgmId;
        switch (boardType) {
            case "basic":
                this.boardType = BoardType.BASIC;
                break;
            case "shorts":
                this.boardType = BoardType.SHORTS;
                break;
            case "advertisement":
                this.boardType = BoardType.ADVERTISEMENT;
                break;
        }
        this.viewLikes = viewLikes ? Option.TRUE : Option.FALSE;
        this.commentable = commentable ? Option.TRUE : Option.FALSE;
    }


}
