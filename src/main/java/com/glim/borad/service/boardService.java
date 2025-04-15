package com.glim.borad.service;

import com.glim.borad.domain.boards;
import com.glim.borad.dto.request.addBoardRequest;
import com.glim.borad.dto.request.updateBoardRequest;
import com.glim.borad.dto.response.viewBoardResponse;
import com.glim.borad.repository.boardRepository;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class boardService {

    private final boardRepository boardRepository;

    @Transactional
    public void insert(addBoardRequest request) {
        boardRepository.save(new addBoardRequest().toEntity(request));
    }

    public void update(Long id, updateBoardRequest request) {
        boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.update(request);
        boardRepository.save(boards);
    }

    public List<viewBoardResponse> list(Long id) {
        return boardRepository.findAllById(id);
    }
}
