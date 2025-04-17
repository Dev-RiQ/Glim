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
import java.util.stream.Collectors;

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
    @Transactional
    public void update(Long id, UpdateBoardRequest request) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.update(request);
        boardRepository.save(boards);
    }

    public List<ViewBoardResponse> list(Long id) {
      /*  return (List<ViewBoardResponse>) boardRepository.findAllById(id).stream();*/
        return null;
    public List<ViewBoardResponse> list() {
        return boardRepository.findAll().stream().map(board -> new ViewBoardResponse(board)).collect(Collectors.toList());
    }
    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public void updateLike(Long id, int like) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setLikes(boards.getLikes() + like);
        boardRepository.save(boards);
    }

    @Transactional
    public void updateView(Long id, int view) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setViews(boards.getViews() + view);
        boardRepository.save(boards);
    }

    @Transactional
    public void updateComment(Long id, int comment) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setComments(boards.getComments() + comment);
        boardRepository.save(boards);
    }

    @Transactional
    public void updateShare(Long id, int share) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setShares(boards.getShares() + share);
        boardRepository.save(boards);
    }
}
