package com.glim.borad.service;

import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void insert(AddBoardRequest request) {
        boardRepository.save(new AddBoardRequest().toEntity(request));
    }
    @Transactional
    public Boards update(Long id, UpdateBoardRequest request) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.update(request);
        boardRepository.save(boards);
        return boards;
    }

    public List<ViewBoardResponse> list(Long userId, Long offset) {
        List<Boards> boardList = offset == null ?
                boardRepository.findAllByUserIdOrderByIdAsc(userId, Limit.of(10)) :
                boardRepository.findAllByUserIdAndIdGreaterThanOrderByIdAsc(userId, offset, Limit.of(10));
        return boardList.stream()
                .map(ViewBoardResponse::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }




    @Transactional
    public Boards updateLike(Long id, int like) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setLikes(boards.getLikes() + like);
        boardRepository.save(boards);
        return boards;
    }

    @Transactional
    public Boards updateView(Long id, int view) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setViews(boards.getViews() + view);
        boardRepository.save(boards);
        return boards;
    }

    @Transactional
    public Boards updateComment(Long id, int comment) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setComments(boards.getComments() + comment);
        boardRepository.save(boards);
        return boards;
    }

    @Transactional
    public void updateShare(Long id, int share) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setShares(boards.getShares() + share);
        boardRepository.save(boards);
    }
}
