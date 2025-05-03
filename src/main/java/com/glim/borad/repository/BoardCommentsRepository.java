package com.glim.borad.repository;

import com.glim.borad.domain.BoardComments;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BoardCommentsRepository extends JpaRepository<BoardComments, Long> {
    List<BoardComments> findAllById(Long id);
    List<BoardComments> findAllByBoardIdOrderByIdAsc(Long boardId, Limit limit);
    List<BoardComments> findAllByBoardIdAndIdGreaterThanOrderByIdAsc(Long boardId, Long offset, Limit limit);

    List<BoardComments> findAllByBoardIdAndReplyCommentIdOrderByIdAsc(Long id, long l, Limit of);

    List<BoardComments> findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(Long id, Long offset, long l, Limit of);

    List<BoardComments> findAllByReplyCommentIdOrderByIdAsc(Long commentId, Limit of);

    List<BoardComments> findAllByReplyCommentIdAndIdGreaterThanOrderByIdAsc(Long commentId, Long offset, Limit of);

    Optional<BoardComments> findByReplyCommentId(Long id, Limit limit);
}