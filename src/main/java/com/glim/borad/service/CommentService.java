package com.glim.borad.service;

import com.glim.borad.domain.BoardComments;
import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
import com.glim.borad.dto.response.ViewReplyCommentResponse;
import com.glim.borad.repository.BoardCommentsRepository;
import com.glim.borad.repository.BoardRepository;
import com.glim.borad.repository.CommentLikeRepository;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.notification.domain.Type;
import com.glim.notification.service.NotificationService;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.User;
import com.glim.user.dto.response.ViewBoardUserResponse;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final BoardCommentsRepository boardCommentsRepository;
    private final UserRepository userRepository;
    private final AwsS3Util awsS3Util;
    private final StoryService storyService;
    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;

    @Transactional
    public ViewCommentsResponse insert(AddCommentsRequest request, Long userId, SecurityUserDto user) {
        BoardComments comment = boardCommentsRepository.save(new AddCommentsRequest().toEntity(request, userId));
        ViewBoardUserResponse viewBoardUserResponse = createUserResponse(userId);

        Boards board = boardRepository.findById(request.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        Long notificationUserId = board.getUserId();
        Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_COMMENT : Type.SHORTS_COMMENT;
        notificationService.send(notificationUserId, type, board.getId(), user);

        if (request.getReplyId() != null && request.getReplyId() != 0L) {
            type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_REPLY_COMMENT : Type.SHORTS_REPLY_COMMENT;
            Long replyUserId = boardCommentsRepository.findById(request.getReplyId()).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)).getUserId();
            notificationUserId = userRepository.findById(replyUserId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)).getId();
            notificationService.send(notificationUserId, type, board.getId(), user);
        }

        return new ViewCommentsResponse(comment, viewBoardUserResponse);
    }

    public List<ViewCommentsResponse> list(Long id, Long offset, SecurityUserDto user) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByBoardIdAndReplyCommentIdOrderByIdAsc(id, 0L, Limit.of(30)).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NO_CREATED)) :
                boardCommentsRepository.findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(id, offset, 0L, Limit.of(30)).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NO_MORE));

        return commentsList.stream().map(comment -> {
            ViewCommentsResponse view = getCommentView(comment, user.getId());
            view.setIsReply(boardCommentsRepository.findByReplyCommentId(view.getId(), Limit.of(1)) != null);
            view.setIsLike(commentLikeRepository.existsByCommentIdAndUserId(view.getId(), user.getId()));
            return view;
        }).collect(Collectors.toList());
    }

    public List<ViewReplyCommentResponse> replyList(Long replyCommentId, Long offset, SecurityUserDto user) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByReplyCommentIdOrderByIdAsc(replyCommentId, Limit.of(30)).orElseThrow(() -> new CustomException(ErrorCode.REPLY_COMMENT_NO_CREATED)) :
                boardCommentsRepository.findAllByReplyCommentIdAndIdGreaterThanOrderByIdAsc(replyCommentId, offset, Limit.of(30)).orElseThrow(() -> new CustomException(ErrorCode.REPLY_COMMENT_NO_CREATED));

        return commentsList.stream().map(comment -> {
            ViewReplyCommentResponse view = getReplyView(comment, user.getId());
            view.setIsLike(commentLikeRepository.existsByCommentIdAndUserId(view.getId(), user.getId()));
            return view;
        }).collect(Collectors.toList());
    }

    private ViewBoardUserResponse createUserResponse(Long userId) {
        User userInfo = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ViewBoardUserResponse response = new ViewBoardUserResponse(userInfo);
        response.setImg(awsS3Util.getURL(response.getImg(), FileSize.IMAGE_128));
        response.setIsMine(true);
        response.setIsStory(storyService.isStory(userId));
        return response;
    }

    private ViewCommentsResponse getCommentView(BoardComments comment, Long userId) {
        ViewBoardUserResponse viewBoardUserResponse = createUserResponse(comment.getUserId());
        viewBoardUserResponse.setIsMine(Objects.equals(userId, comment.getUserId()));
        return new ViewCommentsResponse(comment, viewBoardUserResponse);
    }

    private ViewReplyCommentResponse getReplyView(BoardComments comment, Long userId) {
        ViewBoardUserResponse viewBoardUserResponse = createUserResponse(comment.getUserId());
        viewBoardUserResponse.setIsMine(Objects.equals(userId, comment.getUserId()));
        return new ViewReplyCommentResponse(comment, viewBoardUserResponse);
    }

    @Transactional
    public Long delete(Long commentId, SecurityUserDto user) {
        BoardComments comment = boardCommentsRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        commentLikeRepository.deleteAllByCommentId(comment.getId());
        Long boardId = comment.getBoardId();

        List<BoardComments> replies = boardCommentsRepository.findByReplyCommentId(comment.getId());
        for (BoardComments reply : replies) {
            commentLikeRepository.deleteAllByCommentId(reply.getId());
            boardCommentsRepository.delete(reply);
        }
        boardCommentsRepository.delete(comment);

        Boards board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        board.setComments(board.getComments() - replies.size());
        boardRepository.save(board);

        Long notificationUserId = board.getUserId();
        Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_COMMENT : Type.SHORTS_COMMENT;
        notificationService.delete(notificationUserId, type, boardId, user);

        return boardId;
    }

    @Transactional
    public void updateLike(Long id, int like) {
        BoardComments comment = boardCommentsRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.setLikes(comment.getLikes() + like);
        boardCommentsRepository.save(comment);
    }

    @Transactional
    public void deleteBoardCommentsByUser(Long userId) {
        List<BoardComments> commentsList = boardCommentsRepository.findAllByUserId(userId);
        for (BoardComments comment : commentsList) {
            commentLikeRepository.deleteAllByCommentId(comment.getId());
            boardCommentsRepository.delete(comment);
        }
    }
}
