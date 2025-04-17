package com.glim.borad.service;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
/*@Service*/
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void insert(AddBoardRequest request) {
        boardRepository.save(new AddBoardRequest().toEntity(request));
    }

    public void update(Long id, UpdateBoardRequest request) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.update(request);
        boardRepository.save(boards);
    }

    public List<ViewBoardResponse> list(Long id) {
      /*  return (List<ViewBoardResponse>) boardRepository.findAllById(id).stream();*/
        return null;
    }
}
