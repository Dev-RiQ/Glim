package com.glim.borad.service;

import com.glim.borad.domain.BoardLikes;
import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardLikeRequest;
import com.glim.borad.repository.BoardLikeRepository;
import com.glim.borad.repository.BoardRepository;
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
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;

    @Transactional
    public boolean insert(Long boardId, Long userId, SecurityUserDto user) {
        if(!boardLikeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            boardLikeRepository.save(new AddBoardLikeRequest().toEntity(new AddBoardLikeRequest(boardId, userId)));
            Boards board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            Long notificationUserId = board.getUserId();
            Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_LIKE : Type.SHORTS_LIKE;
            notificationService.send(notificationUserId, type, boardId, user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delete(Long boardId, Long userId, SecurityUserDto user) {
        BoardLikes boardLikes = boardLikeRepository.findByBoardIdAndUserId(boardId,userId);
        if(boardLikes != null){
            boardLikeRepository.delete(boardLikes);
            Boards board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
            Long notificationUserId = board.getUserId();
            Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_LIKE : Type.SHORTS_LIKE;
            notificationService.delete(notificationUserId, type, boardId, user);
            return true;
        }
        return false;
    }

    public Boolean isLike(Long boardId, Long userId) {
        return boardLikeRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    @Transactional
    public void deleteBoardLikesByUser(Long userId) {
        boardLikeRepository.deleteByUserId(userId);
    }

}
