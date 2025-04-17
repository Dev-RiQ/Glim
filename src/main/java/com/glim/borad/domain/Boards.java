package com.glim.borad.domain;


import com.glim.borad.dto.request.UpdateBoardRequest;
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
    @Column(nullable = false)
    private Integer shares;
    @Column(name = "tag_user_ids", nullable = false)
    private String tagUserIds;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "bgm_id", nullable = false)
    private Integer bgmId;
    @Column(name = "board_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;
    @Column(name = "view_likes", nullable = false)
    @Enumerated(EnumType.STRING)
    private Option viewLikes;
    @Column(name = "view_shares", nullable = false)
    @Enumerated(EnumType.STRING)
    private Option viewShares;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Option commentable;

    @Builder
    public Boards(Long userId, String location, String content, String tagUserIds, Integer bgmId, Boolean boardType, Boolean viewLikes, Boolean viewShares, Boolean commentable){
        this.userId = userId;
        this.location = location;
        this.content = content;
        this.tagUserIds = tagUserIds;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likes = 0;
        this.comments = 0;
        this.shares = 0;
        this.views = 0;
        this.bgmId = bgmId;
        this.boardType = boardType ? BoardType.BASIC : BoardType.SHORTS;
        this.viewLikes = viewLikes ? Option.TRUE : Option.FALSE;
        this.viewShares = viewShares ? Option.TRUE : Option.FALSE;
        this.commentable = commentable ? Option.TRUE : Option.FALSE;
    }

    public void update(UpdateBoardRequest request) {
        this.location = request.getLocation();
        this.content = request.getContent();
        this.tagUserIds = request.getTagId();
        this.updatedAt = LocalDateTime.now();
        this.viewLikes = request.getViewLikes() ? Option.TRUE : Option.FALSE;
        this.viewShares = request.getViewShares() ? Option.TRUE : Option.FALSE;
        this.commentable = request.getCommentable() ? Option.TRUE : Option.FALSE;
    }


}
