package com.glim.borad.service;

import com.glim.borad.dto.request.AddCommentLikeRequest;
import com.glim.borad.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void insert(AddCommentLikeRequest request) {
        commentLikeRepository.save(new AddCommentLikeRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        commentLikeRepository.deleteById(id);
    }
}
