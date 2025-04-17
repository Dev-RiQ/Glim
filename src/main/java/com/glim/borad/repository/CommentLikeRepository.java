package com.glim.borad.repository;

import com.glim.borad.domain.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {
}
