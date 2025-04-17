package com.glim.borad.service;

import com.glim.borad.dto.request.AddBoardShareRequest;
import com.glim.borad.repository.BoardShareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardShareService {

    private final BoardShareRepository boardShareRepository;

    @Transactional
    public void insert(AddBoardShareRequest request) {
        boardShareRepository.save(new AddBoardShareRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        boardShareRepository.deleteById(id);
    }
}
