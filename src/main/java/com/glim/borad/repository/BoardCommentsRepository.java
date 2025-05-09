package com.glim.borad.repository;

import com.glim.borad.domain.BoardComments;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BoardCommentsRepository extends JpaRepository<BoardComments, Long> {
    Optional<List<BoardComments>> findAllByBoardIdAndReplyCommentIdOrderByIdAsc(Long id, long l, Limit of);
    Optional<List<BoardComments>> findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(Long id, Long offset, long l, Limit of);
    Optional<List<BoardComments>> findAllByReplyCommentIdOrderByIdAsc(Long commentId, Limit of);
    Optional<List<BoardComments>> findAllByReplyCommentIdAndIdGreaterThanOrderByIdAsc(Long commentId, Long offset, Limit of);
    BoardComments findByReplyCommentId(Long id, Limit limit);
    void deleteByUserId(Long userId);

    List<BoardComments> findAllByBoardId(Long boardId);

    List<BoardComments> findByReplyCommentId(Long id);

    List<BoardComments> findAllByUserId(Long userId);
}