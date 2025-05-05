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
        User userInfo = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(userInfo);
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(true);
        viewBoardUserResponse.setIsStory(storyService.isStory(userId));

        Boards board = boardRepository.findById(request.getBoardId()).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        Long notificationUserId = board.getUserId();
        Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_COMMENT : Type.SHORTS_COMMENT;
        notificationService.send(notificationUserId, type, board.getId(), user);

        if(request.getReplyId() != null && request.getReplyId() != 0L) {
            notificationUserId = userRepository.findById(request.getReplyId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)).getId();
            notificationService.send(notificationUserId, type, board.getId(), user);
        }

        return new ViewCommentsResponse(comment, viewBoardUserResponse);
    }

    public List<ViewCommentsResponse> list(Long id, Long offset, SecurityUserDto user) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByBoardIdAndReplyCommentIdOrderByIdAsc(id, 0L, Limit.of(30))
                        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NO_CREATED))
                :boardCommentsRepository.findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(id, offset, 0L, Limit.of(30))
                        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NO_MORE));
        // 커멘트아이디에 해당 댓글 번호 있는지 없는지
        List<ViewCommentsResponse> list = commentsList.stream().map(comment -> getCommentView(comment, user.getId())).collect(Collectors.toList());
        list.forEach(viewCommentsResponse -> {
            BoardComments obj = boardCommentsRepository.findByReplyCommentId(viewCommentsResponse.getId(), Limit.of(1)).orElse(null);
            viewCommentsResponse.setIsReply(obj != null);
            Boolean isLike = commentLikeRepository.existsByCommentIdAndUserId(viewCommentsResponse.getId(), user.getId());
            viewCommentsResponse.setIsLike(isLike);
        });
        return list;
    }

    public List<ViewReplyCommentResponse> replyList(Long replyCommentId, Long offset, SecurityUserDto user) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByReplyCommentIdOrderByIdAsc(replyCommentId, Limit.of(30))
                        .orElseThrow(() -> new CustomException(ErrorCode.REPLY_COMMENT_NO_CREATED))
                :boardCommentsRepository.findAllByReplyCommentIdAndIdGreaterThanOrderByIdAsc(replyCommentId, offset, Limit.of(30))
                        .orElseThrow(() -> new CustomException(ErrorCode.REPLY_COMMENT_NO_CREATED));
        List<ViewReplyCommentResponse> list = commentsList.stream().map(comment -> getReplyView(comment, user.getId())).collect(Collectors.toList());
        list.forEach(viewCommentsResponse -> {
            Boolean isLike = commentLikeRepository.existsByCommentIdAndUserId(viewCommentsResponse.getId(), user.getId());
            viewCommentsResponse.setIsLike(isLike);
        });
        return list;
    }

    private ViewCommentsResponse getCommentView(BoardComments comment, Long userId){
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(Objects.equals(userId, comment.getUserId()));
        viewBoardUserResponse.setIsStory(storyService.isStory(comment.getUserId()));
        return new ViewCommentsResponse(comment, viewBoardUserResponse);
    }
    private ViewReplyCommentResponse getReplyView(BoardComments comment, Long userId){
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(userRepository.findById(comment.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(Objects.equals(userId, comment.getUserId()));
        viewBoardUserResponse.setIsStory(storyService.isStory(comment.getUserId()));
        return new ViewReplyCommentResponse(comment, viewBoardUserResponse);
    }

    @Transactional
    public Long delete(Long commentId, SecurityUserDto user) {
        BoardComments comments = boardCommentsRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Long boardId = comments.getBoardId();
        List<BoardComments> commentsList = boardCommentsRepository.findByReplyCommentId(comments.getId());
        for (BoardComments comment : commentsList) {
            commentLikeRepository.deleteAllByCommentId(comment.getId());
            boardCommentsRepository.delete(comment);
        }
        boardCommentsRepository.delete(comments);

        Boards board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        Long notificationUserId = board.getUserId();
        Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_COMMENT : Type.SHORTS_COMMENT;
        notificationService.delete(notificationUserId, type, boardId, user);

        return boardId;
    }

    @Transactional
    public void updateLike(Long id, int like) {
        BoardComments boardComments = boardCommentsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        boardComments.setLikes(boardComments.getLikes() + like);
        boardCommentsRepository.save(boardComments);
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
