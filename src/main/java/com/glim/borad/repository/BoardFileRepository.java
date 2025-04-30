package com.glim.borad.repository;

import com.glim.borad.domain.BoardFiles;
import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFiles, Long> {
    List<BoardFiles> findByBoardIdAndFileTypeOrderByBoardFileIdAsc(Long boardId, FileType type);
}
