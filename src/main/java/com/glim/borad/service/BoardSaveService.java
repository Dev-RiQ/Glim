package com.glim.borad.service;

import com.glim.borad.domain.BoardSaves;
import com.glim.borad.dto.request.AddBoardSaveRequest;
import com.glim.borad.repository.BoardRepository;
import com.glim.borad.repository.BoardSaveRepository;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardSaveService {

    private final BoardSaveRepository boardSaveRepository;

    @Transactional
    public void insert(Long boardId, Long userId) {
        boardSaveRepository.save(new AddBoardSaveRequest().toEntity(new AddBoardSaveRequest(boardId, userId)));
    }

    @Transactional
    public void delete(Long boardId, Long userId) {
        BoardSaves boardSaves = boardSaveRepository.findByBoardIdAndUserId(boardId, userId);
        if(boardSaves != null) {
            boardSaveRepository.delete(boardSaves);
        }
    }

    public List<Long> getSaveList(Long userId) {
        return boardSaveRepository.findByUserId(userId)
                .stream()
                .map(BoardSaves::getBoardId) // boardId만 추출
                .collect(Collectors.toList());
    }

}
