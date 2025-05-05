package com.glim.borad.service;

import com.glim.borad.domain.BoardComments;
import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.domain.CommentLikes;
import com.glim.borad.dto.request.AddCommentLikeRequest;
import com.glim.borad.repository.BoardCommentsRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.borad.repository.CommentLikeRepository;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.notification.domain.Type;
import com.glim.notification.service.NotificationService;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;
    private final BoardCommentsRepository boardCommentsRepository;
    private final NotificationService notificationService;

    @Transactional
    public boolean insert(AddCommentLikeRequest request, SecurityUserDto user) {
        if(!commentLikeRepository.existsByCommentIdAndUserId(request.getCommentId(),request.getUserId())) {
            commentLikeRepository.save(new AddCommentLikeRequest().toEntity(request));
            BoardComments boardComments = boardCommentsRepository.findById(request.getCommentId()).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
            Boards board = boardRepository.findById(boardComments.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            Long notificationUserId = board.getUserId();
            Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_COMMENT_LIKE : Type.SHORTS_COMMENT_LIKE;
            notificationService.send(notificationUserId, type, board.getId(), user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delete(Long commnetId, Long userId, SecurityUserDto user) {
        CommentLikes commentLike = commentLikeRepository.findByCommentIdAndUserId(commnetId,userId);
        if(commentLike != null) {
            commentLikeRepository.delete(commentLike);
            BoardComments boardComments = boardCommentsRepository.findById(commnetId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
            Boards board = boardRepository.findById(boardComments.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            Long notificationUserId = board.getUserId();
            Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_COMMENT_LIKE : Type.SHORTS_COMMENT_LIKE;
            notificationService.delete(notificationUserId, type, board.getId(), user);

            return true;
        }
        return false;
    }

    @Transactional
    public void deleteCommentLikesByUser(Long userId) {
        commentLikeRepository.deleteByUserId(userId);
    }
}
