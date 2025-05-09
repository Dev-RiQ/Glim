package com.glim.borad.service;

import com.glim.borad.dto.request.AddBoardViewRequest;
import com.glim.borad.repository.BoardViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardViewService {

    private final BoardViewRepository boardViewRepository;

    @Transactional
    public boolean insert(AddBoardViewRequest request) {
        if(!boardViewRepository.existsByUserIdAndBoardId(request.getUserId(), request.getBoardId())) {
            boardViewRepository.save(new AddBoardViewRequest().toEntity(request));
            return true;
        };
        return false;
    }
}
