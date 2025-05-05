package com.glim.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {

    BOARD_LIKE("님이 회원님의 게시글을 좋아합니다.","/board/"),
    SHORTS_LIKE("님이 회원님의 숏츠를 좋아합니다.","/shorts/"),
    STORY_LIKE("님이 회원님의 스토리를 좋아합니다.","/story/"),
    BOARD_COMMENT_LIKE("님이 회원님의 댓글을 좋아합니다.","/board/"),
    SHORTS_COMMENT_LIKE("님이 회원님의 댓글을 좋아합니다.","/shorts/"),

    BOARD_COMMENT("님이 회원님의 게시글에 댓글을 달았습니다.", "/board/"),
    SHORTS_COMMENT("님이 회원님의 숏츠에 댓글을 달았습니다.", "/shorts/"),
    BOARD_REPLY_COMMENT("님이 회원님의 댓글에 답글을 달았습니다.", "/board/"),
    SHORTS_REPLY_COMMENT("님이 회원님의 댓글에 답글을 달았습니다.", "/shorts/"),

    BOARD_TAG("님의 게시글에 태그 되었습니다.", "/board/"),
    SHORTS_TAG("님의 숏츠에 태그 되었습니다.", "/shorts/"),

    FOLLOW("님이 회원님을 팔로우했습니다.", "/myPage/");

    private final String message;
    private final String uri;

}
