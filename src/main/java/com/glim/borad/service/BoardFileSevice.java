package com.glim.borad.service;

import com.glim.borad.domain.BoardFiles;
import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.dto.request.UpdateBoardFileRequest;
import com.glim.borad.repository.BoardFileRepository;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardFileSevice {

    private final BoardFileRepository boardFileRepository;

    @Transactional
    public void insert(AddBoardFileRequest request) {
        boardFileRepository.save(new AddBoardFileRequest().toEntity(request));
    }

    @Transactional
    public void update(Long id, UpdateBoardFileRequest request) {
        BoardFiles boardFiles = boardFileRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boardFiles.update(request);
        boardFileRepository.save(boardFiles);
    }

    @Transactional
    public void delete(Long id) {
        boardFileRepository.deleteById(id);
    }
}
