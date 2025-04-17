package com.glim.borad.repository;

import com.glim.borad.domain.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLikes, Long> {
}
