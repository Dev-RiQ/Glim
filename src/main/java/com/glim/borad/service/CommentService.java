package com.glim.borad.service;

import com.glim.borad.domain.BoardComments;
import com.glim.borad.dto.request.AddCommentsRequest;
import com.glim.borad.dto.response.ViewCommentsResponse;
import com.glim.borad.dto.response.ViewReplyCommentResponse;
import com.glim.borad.repository.BoardCommentsRepository;
import com.glim.borad.repository.CommentLikeRepository;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
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

    @Transactional
    public ViewCommentsResponse insert(AddCommentsRequest request, Long userId) {
        BoardComments comment = boardCommentsRepository.save(new AddCommentsRequest().toEntity(request, userId));
        User user = userRepository.findUserById(userId);
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(user);
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(true);
        viewBoardUserResponse.setIsStory(storyService.isStory(userId));
        return new ViewCommentsResponse(comment, viewBoardUserResponse);
    }

    public List<ViewCommentsResponse> list(Long id, Long offset, SecurityUserDto user) {
        List<BoardComments> commentsList = offset == null ?
                boardCommentsRepository.findAllByBoardIdAndReplyCommentIdOrderByIdAsc(id, 0L, Limit.of(30)) :
                boardCommentsRepository.findAllByBoardIdAndIdGreaterThanAndReplyCommentIdOrderByIdAsc(id, offset, 0L, Limit.of(30));
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
                boardCommentsRepository.findAllByReplyCommentIdOrderByIdAsc(replyCommentId, Limit.of(30)) :
                boardCommentsRepository.findAllByReplyCommentIdAndIdGreaterThanOrderByIdAsc(replyCommentId, offset, Limit.of(30));
        List<ViewReplyCommentResponse> list = commentsList.stream().map(comment -> getReplyView(comment, user.getId())).collect(Collectors.toList());
        list.forEach(viewCommentsResponse -> {
            Boolean isLike = commentLikeRepository.existsByCommentIdAndUserId(viewCommentsResponse.getId(), user.getId());
            viewCommentsResponse.setIsLike(isLike);
        });
        return list;
    }

    private ViewCommentsResponse getCommentView(BoardComments comment, Long userId){
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(userRepository.findById(comment.getUserId()).orElseThrow(ErrorCode::throwDummyNotFound));
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(Objects.equals(userId, comment.getUserId()));
        viewBoardUserResponse.setIsStory(storyService.isStory(comment.getUserId()));
        return new ViewCommentsResponse(comment, viewBoardUserResponse);
    }
    private ViewReplyCommentResponse getReplyView(BoardComments comment, Long userId){
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(userRepository.findById(comment.getUserId()).orElseThrow(ErrorCode::throwDummyNotFound));
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(Objects.equals(userId, comment.getUserId()));
        viewBoardUserResponse.setIsStory(storyService.isStory(comment.getUserId()));
        return new ViewReplyCommentResponse(comment, viewBoardUserResponse);
    }


    @Transactional
    public Long delete(Long commentId) {
        BoardComments comments = boardCommentsRepository.findById(commentId).orElseThrow(ErrorCode::throwDummyNotFound);
        Long boardId = comments.getBoardId();
        boardCommentsRepository.delete(comments);
        return boardId;
    }

    @Transactional
    public void updateLike(Long id, int like) {
        BoardComments boardComments = boardCommentsRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boardComments.setLikes(boardComments.getLikes() + like);
        boardCommentsRepository.save(boardComments);
    }

    @Transactional
    public void deleteBoardCommentsByUser(Long userId) {
        boardCommentsRepository.deleteByUserId(userId);
    }

}
