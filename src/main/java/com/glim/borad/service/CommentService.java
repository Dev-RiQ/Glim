package com.glim.borad.service;

import com.glim.borad.domain.BoardComments;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.request.UpdateCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
import com.glim.borad.repository.BoardCommentsRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final BoardCommentsRepository boardCommentsRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @Transactional
    public void insert(AddCommentsRequest request) {
        boardCommentsRepository.save(new AddCommentsRequest().toEntity(request));
    }

    public List<ViewCommentsResponse> list(Long id, Long offset) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByBoardIdAndReplyCommentIdOrderByIdAsc(id, 0L, Limit.of(30)) :
                boardCommentsRepository.findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(id, offset, 0L, Limit.of(30));
        // 커멘트아이디에 해당 댓글 번호 있는지 없는지
        List<ViewCommentsResponse> list = commentsList.stream().map(ViewCommentsResponse::new).collect(Collectors.toList());
        list.forEach(viewCommentsResponse -> {

            BoardComments obj = boardCommentsRepository.findByReplyCommentId(viewCommentsResponse.getId()).orElse(null);

            viewCommentsResponse.setIsReply(obj == null ? false : true);
        });
        return list;
    }

    public List<ViewCommentsResponse> replyList(Long replyCommentId, Long offset) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByReplyCommentIdOrderByIdAsc(replyCommentId, Limit.of(30)) :
                boardCommentsRepository.findAllByReplyCommentIdAndIdGreaterThanOrderByIdAsc(replyCommentId, offset, Limit.of(30));
        return commentsList.stream().map(ViewCommentsResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, UpdateCommentsRequest request) {
        BoardComments comments = boardCommentsRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        comments.update(request);
        boardCommentsRepository.save(comments);
    }
    @Transactional
    public void delete(Long boardId, Long userId) {
        Boards board = boardRepository.findById(boardId).orElseThrow(ErrorCode::throwDummyNotFound);
        boardCommentsRepository.deleteById(boardId);
        boardService.updateComment(boardId, -1);
    }

    @Transactional
    public BoardComments updateLike(Long id, int like) {
        BoardComments boardComments = boardCommentsRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boardComments.setLikes(boardComments.getLikes() + like);
        boardCommentsRepository.save(boardComments);
        return boardComments;
    }

}
