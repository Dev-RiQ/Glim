package com.glim.borad.repository;

import com.glim.borad.domain.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    CommentLikes findByCommentIdAndUserId(Long commnetId, Long userId);

    void deleteByUserId(Long userId);

    void deleteAllByCommentId(Long id);
}
