package com.glim.borad.repository;

import com.glim.borad.domain.boards;
import com.glim.borad.dto.response.viewBoardResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface boardRepository extends JpaRepository<boards, Long> {
    List<viewBoardResponse> findAllById(Long id);
}
