package com.glim.borad.service;

import com.glim.borad.domain.BoardTags;
import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.repository.BoardRepository;
import com.glim.borad.repository.BoardTagRepository;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardTagService {

    private final BoardTagRepository boardTagRepository;
    private final BoardRepository boardRepository;

//    @Transactional
//    public void insert(AddBoardTagRequest request) {
//        boardTagRepository.save(new AddBoardTagRequest().toEntity(request));
//    }

    @Transactional
    public void delete(Long id) {
        boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boardTagRepository.deleteById(id);
    }
}
