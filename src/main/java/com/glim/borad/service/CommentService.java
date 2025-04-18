package com.glim.borad.service;

import com.glim.borad.domain.BoardComments;
import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.request.UpdateCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
import com.glim.borad.repository.BoardCommentsRepository;
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

    @Transactional
    public void insert(AddCommentsRequest request) {
        boardCommentsRepository.save(new AddCommentsRequest().toEntity(request));
    }

    public List<ViewCommentsResponse> list(Long id, Long offset) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByBoardIdAndReplyCommentIdOrderByIdAsc(id, 0L, Limit.of(30)) :
                boardCommentsRepository.findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(id, offset, 0L, Limit.of(30));
        return commentsList.stream().map(ViewCommentsResponse::new).collect(Collectors.toList());
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
    public void delete(Long id) {
        boardCommentsRepository.deleteById(id);
    }

    @Transactional
    public BoardComments updateLike(Long id, int like) {
        BoardComments boardComments = boardCommentsRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boardComments.setLikes(boardComments.getLikes() + like);
        boardCommentsRepository.save(boardComments);
        return boardComments;
    }

}
