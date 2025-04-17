package com.glim.borad.service;

import com.glim.borad.dto.request.AddBoardSaveRequest;
import com.glim.borad.repository.BoardSaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardSaveService {

    private final BoardSaveRepository boardSaveRepository;

    @Transactional
    public void insert(AddBoardSaveRequest request) {
        boardSaveRepository.save(new AddBoardSaveRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        boardSaveRepository.deleteById(id);
    }
}
