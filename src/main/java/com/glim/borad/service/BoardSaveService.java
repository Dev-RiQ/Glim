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
    private final BoardRepository boardRepository;

    @Transactional
    public void insert(AddBoardSaveRequest request) {
        boardSaveRepository.save(new AddBoardSaveRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long boardId, Long userId) {
        boardRepository.findById(boardId).orElseThrow(ErrorCode::throwDummyNotFound);
        boardSaveRepository.deleteByBoardIdAndUserId(boardId, userId);
    }

    public List<Long> getSaveList(Long userId) {
        return boardSaveRepository.findByUserId(userId)
                .stream()
                .map(BoardSaves::getBoardId) // boardId만 추출
                .collect(Collectors.toList());
    }
}
