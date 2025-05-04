package com.glim.borad.service;

import com.glim.borad.domain.CommentLikes;
import com.glim.borad.dto.request.AddCommentLikeRequest;
import com.glim.borad.repository.BoardRepository;
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
    public boolean insert(AddCommentLikeRequest request) {
        if(!commentLikeRepository.existsByCommentIdAndUserId(request.getCommentId(),request.getUserId())) {
            commentLikeRepository.save(new AddCommentLikeRequest().toEntity(request));
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delete(Long commnetId, Long userId) {
        CommentLikes commentLike = commentLikeRepository.findByCommentIdAndUserId(commnetId,userId);
        if(commentLike != null) {
            commentLikeRepository.delete(commentLike);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteCommentLikesByUser(Long userId) {
        commentLikeRepository.deleteByUserId(userId);
    }
}
