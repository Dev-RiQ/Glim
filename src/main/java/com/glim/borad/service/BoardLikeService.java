package com.glim.borad.service;

import com.glim.borad.domain.BoardLikes;
import com.glim.borad.dto.request.AddBoardLikeRequest;
import com.glim.borad.repository.BoardLikeRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.exception.ErrorCode;
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

    @Transactional
    public boolean insert(Long boardId, Long userId) {
        if(!boardLikeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            boardLikeRepository.save(new AddBoardLikeRequest().toEntity(new AddBoardLikeRequest(boardId, userId)));
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delete(Long boardId, Long userId) {
        BoardLikes boardLikes = boardLikeRepository.findByBoardIdAndUserId(boardId,userId);
        if(boardLikes != null){
            boardLikeRepository.delete(boardLikes);
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
