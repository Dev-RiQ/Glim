package com.glim.borad.service;

import com.glim.borad.dto.request.AddBoardLikeRequest;
import com.glim.borad.repository.BoardLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;

    @Transactional
    public void insert(AddBoardLikeRequest request) {
        boardLikeRepository.save(new AddBoardLikeRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        boardLikeRepository.deleteById(id);
    }
}
