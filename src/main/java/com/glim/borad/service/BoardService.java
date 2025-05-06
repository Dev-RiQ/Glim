package com.glim.borad.service;

import com.glim.admin.domain.Advertisement;
import com.glim.admin.repository.AdvertisementRepository;
import com.glim.admin.service.AdvertisementService;
import com.glim.borad.domain.*;
import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.dto.response.ViewMyPageBoardResponse;
import com.glim.borad.repository.*;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.service.AwsS3Service;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.notification.domain.Type;
import com.glim.notification.service.NotificationService;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.Follow;
import com.glim.user.domain.User;
import com.glim.user.dto.response.ViewBoardUserResponse;
import com.glim.user.repository.FollowRepository;
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
public class BoardService {

    private final AwsS3Util awsS3Util;
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardTagRepository boardTagRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BgmRepository bgmRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final FollowRepository followRepository;
    private final StoryService storyService;
    private final BoardSaveRepository boardSaveRepository;
    private final CommentLikeService commentLikeService;
    private final CommentService commentService;
    private final BoardLikeService boardLikeService;
    private final NotificationService notificationService;
    private final AwsS3Service awsS3Service;
    private final BoardViewRepository boardViewRepository;
    private final BoardCommentsRepository boardCommentsRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final AdvertisementService advertisementService;

    @Transactional
    public void insert(AddBoardRequest request, SecurityUserDto user) {
        Boards board = boardRepository.save(new AddBoardRequest().toEntity(request));
        for (int i = 0; i < request.getImg().size(); i++) {
            boardFileRepository.save(new AddBoardFileRequest().toEntity(board.getId(), request.getImg().get(i), String.valueOf(board.getBoardType())));
        }
        for (int i = 0; i < request.getTags().size(); i++) {
            boardTagRepository.save(new AddBoardTagRequest().toEntity(board.getId(), request.getTags().get(i)));
        }
        if(board.getTagUserIds() != null){
            sendNotificationInTagUsers(board, user);
        }
        if(board.getBoardType().equals(BoardType.ADVERTISEMENT)){
            advertisementService.create(board.getId());
        }

    }

    private void sendNotificationInTagUsers(Boards board, SecurityUserDto user){
        String tagUserIds = board.getTagUserIds().substring(1, board.getTagUserIds().length() - 1);
        String[] tags = tagUserIds.split(", ");
        for(String id : tags){
            try{
                Long notificationUserId = Long.parseLong(id);
                Type type = board.getBoardType().equals(BoardType.BASIC) ? Type.BOARD_TAG : Type.SHORTS_TAG;
                notificationService.send(notificationUserId, type, board.getId(), user);
            }catch (Exception e){
                throw new CustomException(ErrorCode.TAG_NOT_FOUND);
            }
        }
    }

    public List<ViewBoardResponse> getMainBoard(Long id, Long offset) {
//        List<Follow> followList = followRepository.findAllByFollowerUserId(id);
//        List<Long> followedUserIds = followList.stream().map(Follow::getFollowingUserId).collect(Collectors.toList());
//        List<Boards> boardList = (offset == null) ? boardRepository.findAllByUserIdInOrderByIdDesc(followedUserIds, Limit.of(10))
//                : boardRepository.findAllByUserIdInAndIdLessThanOrderByIdDesc(followedUserIds, offset, Limit.of(10));
//        if (boardList.size() < 10) {
////            User user = (User) userRepository.findAllById(Collections.singleton(id));
////            System.out.println("userTag = " + user.getTags());
//        }
//        if (boardList.size() < 10) {
//            int remain = 10 - boardList.size();
//            List<Long> excludeIds = boardList.stream().map(Boards::getId).collect(Collectors.toList());
//            List<Boards> fillBoards = (offset == null) ? boardRepository.findAllByIdNotInOrderByIdDesc(excludeIds, Limit.of(remain))
//                    : boardRepository.findAllByIdNotInAndIdLessThanOrderByIdDesc(excludeIds, offset, Limit.of(remain));
//
//            boardList.addAll(fillBoards);
//        }
        List<Boards> boardList = offset == null ? boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.BASIC, Limit.of(10))
                :boardRepository.findAllByBoardTypeAndIdLessThanOrderByIdDesc(BoardType.BASIC, offset, Limit.of(10));
        List<ViewBoardResponse> list = boardList.stream().map((board) -> getView(board, id)).collect(Collectors.toList());

        User user = userRepository.findById(id).orElse(null);
        if(user != null && user.getRate() == 0){
            ViewBoardResponse ad = getRandomAdvertisement(id);
            if(ad != null){
                list.add(5,ad);
                list.add(getRandomAdvertisement(id));
            }
        }
        list = getSubBoard(list, id);
        return list;
    }

    private ViewBoardResponse getView(Boards board,Long id){
        ViewBoardUserResponse viewBoardUserResponse = new ViewBoardUserResponse(userRepository.findById(board.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
        viewBoardUserResponse.setImg(awsS3Util.getURL(viewBoardUserResponse.getImg(), FileSize.IMAGE_128));
        viewBoardUserResponse.setIsMine(Objects.equals(id, board.getUserId()));
        viewBoardUserResponse.setIsStory(storyService.isStory(board.getUserId()));
        ViewBgmResponse viewBgmResponse = null;
        if(board.getBgmId() != 0){
            Bgms bgm  = bgmRepository.findById(board.getBgmId()).orElseThrow(() -> new CustomException(ErrorCode.BGM_NOT_FOUND));
            viewBgmResponse = new ViewBgmResponse(bgm, awsS3Util.getURL(bgm.getFileName(), FileSize.AUDIO));
        }
        return new ViewBoardResponse(board, viewBoardUserResponse, viewBgmResponse);
    }

    public List<ViewBoardResponse> getSubBoard(List<ViewBoardResponse> list, Long id) {
        list.forEach(viewBoardResponse -> {
            boolean isLike = boardLikeRepository.existsByBoardIdAndUserId(viewBoardResponse.getId(), id);
            boolean isSave = boardSaveRepository.existsByBoardIdAndUserId(viewBoardResponse.getId(), id);
            List<BoardFiles> files = boardFileRepository.findAllByBoardId(viewBoardResponse.getId());
            for (BoardFiles file : files) {
                switch (file.getFileType()) {
                    case IMAGE ->{
                        viewBoardResponse.getImg().add(awsS3Util.getURL(file.getFileName(), FileSize.IMAGE_512));
                    }
                    case VIDEO -> {
                        viewBoardResponse.getImg().add(awsS3Util.getURL(file.getFileName(), FileSize.VIDEO));
                        viewBoardResponse.getImg().add(awsS3Util.getURL(file.getFileName(), FileSize.VIDEO_THUMBNAIL));
                    }
                }
            }
            viewBoardResponse.setIsLike(isLike);
            viewBoardResponse.setIsSave(isSave);
        });
        return list;
    }

    public List<ViewMyPageBoardResponse> myPageBoardList(Long userId, Long offset) {
        List<Boards> boardList = offset == null ? boardRepository.findAllByUserIdAndBoardTypeOrderByIdDesc(userId,BoardType.BASIC, Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_CREATED))
                : boardRepository.findAllByUserIdAndBoardTypeAndIdLessThanOrderByIdDesc(userId,BoardType.BASIC, offset, Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_MORE));
        return boardList.stream().map(this::getByPageBoard).collect(Collectors.toList());
    }
    public List<ViewMyPageBoardResponse> myPageShortsList(Long userId, Long offset) {
        List<Boards> boardList = offset == null ? boardRepository.findAllByUserIdAndBoardTypeOrderByIdDesc(userId,BoardType.SHORTS, Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_CREATED))
                : boardRepository.findAllByUserIdAndBoardTypeAndIdLessThanOrderByIdDesc(userId,BoardType.SHORTS, offset, Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_MORE));
        return boardList.stream().map(this::getByPageBoard).collect(Collectors.toList());
    }
    public List<ViewMyPageBoardResponse> myPageTagsList(Long userId, Long offset) {
        List<Boards> boardList = offset == null ? boardRepository.findAllByTagUserIdsContainingOrderByIdDesc(userId.toString(), Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_CREATED))
                : boardRepository.findAllByTagUserIdsContainingAndIdLessThanOrderByIdDesc(userId.toString(), offset, Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_MORE));
        return boardList.stream().map(this::getByPageBoard).collect(Collectors.toList());
    }
    public List<ViewMyPageBoardResponse> allList(Long offset) {
        List<Boards> boardList = offset == null ? boardRepository.findAllByOrderByIdDesc(Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_CREATED))
                : boardRepository.findAllByIdLessThanOrderByIdDesc(offset, Limit.of(20))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_MORE));
        return boardList.stream().map(this::getByPageBoard).collect(Collectors.toList());
    }
    public List<ViewMyPageBoardResponse> myPageSaveList(Long userId, Long offset) {
        List<BoardSaves> saveList = offset == null ? boardSaveRepository.findAllByUserIdOrderByIdDesc(userId, Limit.of(30))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_CREATED))
                : boardSaveRepository.findAllByUserIdAndIdLessThanOrderByIdDesc(userId, offset, Limit.of(30))
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NO_MORE));
        List<Boards> boardList = boardRepository.findAllByIdIn(saveList.stream().map(BoardSaves::getBoardId).toList())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARDSAVE_NOT_FOUND));
        return boardList.stream().map(this::getByPageBoard).collect(Collectors.toList());
    }

    private ViewMyPageBoardResponse getByPageBoard(Boards board){
        BoardFiles file = boardFileRepository.findAllByBoardId(board.getId(), Limit.of(1));
        String img;
        if(board.getBoardType().equals(BoardType.BASIC)){
            img = awsS3Util.getURL(file.getFileName(), FileSize.IMAGE_128);
        }else{
            img = awsS3Util.getURL(file.getFileName(), FileSize.VIDEO_THUMBNAIL);
        }
        return new ViewMyPageBoardResponse(board, img);
    }

    @Transactional
    public void delete(Boards board) {
        boardDataDelete(board);
    }
    @Transactional
    public void delete(Long id) {
        Boards board = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boardDataDelete(board);
    }

    private void boardDataDelete(Boards board) {
        Long boardId = board.getId();
        List<BoardFiles> fileList = boardFileRepository.findAllByBoardId(boardId);
        for (BoardFiles file : fileList) {
            awsS3Service.deleteFile(file.getFileName(), board.getBoardType().equals(BoardType.BASIC) ? FileType.IMAGE : FileType.VIDEO);
            boardFileRepository.delete(file);
        }
        boardLikeRepository.deleteAllByBoardId(boardId);
        boardSaveRepository.deleteAllByBoardId(boardId);
        boardTagRepository.deleteAllByBoardId(boardId);
        boardViewRepository.deleteAllByBoardId(boardId);
        List<BoardComments> commentList = boardCommentsRepository.findAllByBoardId(boardId);
        for (BoardComments comment : commentList) {
            commentLikeRepository.deleteAllByCommentId(comment.getId());
            boardCommentsRepository.delete(comment);
        }
        if(board.getBoardType().equals(BoardType.ADVERTISEMENT)){
            advertisementRepository.deleteByBoardId(board.getId());
        }
        boardRepository.delete(board);
    }


    @Transactional
    public void updateLike(Long id, int like) {
        Boards boards = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boards.setLikes(boards.getLikes() + like);
        boardRepository.save(boards);
    }

    @Transactional
    public void updateView(Long id, int view) {
        Boards boards = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boards.setViews(boards.getViews() + view);
        boardRepository.save(boards);
    }

    @Transactional
    public void updateComment(Long id, int comment) {
        Boards boards = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boards.setComments(boards.getComments() + comment);
        boardRepository.save(boards);
    }

    // 해당 회원의 총 게시글 수
    public int countBoardsByUserId(Long userId) {
        return boardRepository.countByUserId(userId);
    }

    public List<ViewMyPageBoardResponse> getSaveList(List<Long> boardIdList) {
        return boardRepository.findByIdIn(boardIdList).stream().map(this::getByPageBoard).toList();
    }

    public ViewBoardResponse getRandomAdvertisement(Long userId) {
        Advertisement ad = advertisementRepository.findRandomApprovedAdvertisement();
        if (ad != null) {
            Long id = ad.getBoardId();
            return getView(boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)), userId);
        }
        return null;
    }

    public boolean isLoginUser(Long id, Long userId) {
        Boards boards = boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return boards.getUserId().equals(userId);
    }

    public ViewBoardResponse getBoard(Long userId, Long id) {
        ViewBoardResponse boardView = getView(boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)),userId);
        return getSubBoard(List.of(boardView), userId).get(0);
    }

    public ViewBoardResponse getShorts(Long userId, Long id) {
        ViewBoardResponse boardView = getView(boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND)),userId);
        return getSubBoard(List.of(boardView), userId).get(0);
    }

    public List<ViewBoardResponse> getShortsList(Long offset, Long userId) {
        List<Boards> boardList = (offset == null)
                ? boardRepository.findAllByBoardTypeOrderByIdDesc(BoardType.SHORTS, Limit.of(5))
                : boardRepository.findAllByBoardTypeAndIdLessThanOrderByIdDesc(BoardType.SHORTS, offset, Limit.of(5));

        List<ViewBoardResponse> list = boardList.stream()
                .map((board) -> getView(board, userId))
                .collect(Collectors.toList());

        list = getSubBoard(list, userId);


        return list;
    }

    @Transactional
    public void deleteBoardRelatedDataByUser(Long userId) {
        List<Boards> boardList = boardRepository.findAllByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        for (Boards board : boardList) {
            delete(board);
        }
        boardSaveRepository.deleteAllByUserId(userId);
        boardLikeService.deleteBoardLikesByUser(userId);
        commentService.deleteBoardCommentsByUser(userId);
        commentLikeService.deleteCommentLikesByUser(userId);
    }


//    public List<ViewBoardResponse> getMyShortsList(Long offset, Long userId) {
//        List<Boards> boardList = (offset == null)
//                ? boardRepository.findAllByUserIdAndBoardTypeOrderByIdDesc(userId, BoardType.SHORTS, Limit.of(20))
//                : boardRepository.findAllByUserIdAndBoardTypeAndIdLessThanOrderByIdDesc(userId, BoardType.SHORTS, offset, Limit.of(20));
//
//        List<ViewBoardResponse> list = boardList.stream()
//                .map((board) -> getView(board, userId))
//                .collect(Collectors.toList());
//
//
//        return list;
//    }

//    public List<ViewBoardResponse> getTagList(Long id) {
//        List<Boards> list = boardRepository.findAllByUserId(id);
//        list.forEach(lists -> {
//
//        });
////
////    @Transactional
////    public Boards update(Long id, UpdateBoardRequest request) {
////        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
////        boards.update(request);
////        boardRepository.save(boards);
////        return boards;
////    }
//    }
}
