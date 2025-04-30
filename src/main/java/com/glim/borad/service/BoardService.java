package com.glim.borad.service;

import com.glim.admin.repository.AdvertisementRepository;
import com.glim.borad.domain.Bgms;
import com.glim.borad.domain.BoardFiles;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.repository.*;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.ErrorCode;
import com.glim.user.domain.Follow;
import com.glim.user.domain.User;
import com.glim.user.repository.FollowRepository;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.List;
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

    @Transactional
    public void insert(AddBoardRequest request) {
        Boards board = boardRepository.save(new AddBoardRequest().toEntity(request));
        for (int i = 0; i < request.getImg().size(); i++) {
            boardFileRepository.save(new AddBoardFileRequest().toEntity(board.getId(), request.getImg().get(i), String.valueOf(board.getBoardType())));
        }
        for (int i = 0; i < request.getTags().size(); i++) {
            boardTagRepository.save(new AddBoardTagRequest().toEntity(board.getId(), request.getTags().get(i)));
        }

    }

    @Transactional
    public Boards update(Long id, UpdateBoardRequest request) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.update(request);
        boardRepository.save(boards);
        return boards;
    }

    public List<ViewBoardResponse> getMainBoard(Long id, Long offset) {
        List<Follow> followList = followRepository.findAllByFollowerUserId(id);
        List<Long> followedUserIds = followList.stream().map(Follow::getFollowingUserId).collect(Collectors.toList());
        User user1 = (User) userRepository.findAllById(Collections.singleton(id));
        System.out.println("userTag = " + user1.getTags());
        List<Boards> boardList = (offset == null) ? boardRepository.findAllByUserIdInOrderByIdDesc(followedUserIds, Limit.of(10)) : boardRepository.findAllByUserIdInAndIdLessThanOrderByIdDesc(followedUserIds, offset, Limit.of(10));
        if (boardList.size() < 10) {
            User user = (User) userRepository.findAllById(Collections.singleton(id));
            System.out.println("userTag = " + user.getTags());
        }
        if (boardList.size() < 10) {
            int remain = 10 - boardList.size();
            List<Long> excludeIds = boardList.stream().map(Boards::getId).collect(Collectors.toList());
            List<Boards> fillBoards = (offset == null) ? boardRepository.findAllByIdNotInOrderByIdDesc(excludeIds, Limit.of(remain)) : boardRepository.findAllByIdNotInAndIdLessThanOrderByIdDesc(excludeIds, offset, Limit.of(remain));

            boardList.addAll(fillBoards);
        }

        List<ViewBoardResponse> list = boardList.stream().map(ViewBoardResponse::new).collect(Collectors.toList());
        list = getSubBoard(list, id);

        return list;
    }

    public List<ViewBoardResponse> getSubBoard(List<ViewBoardResponse> list, Long id) {
        list.forEach(viewBoardResponse -> {
            boolean isLike = boardLikeRepository.existsByBoardIdAndUserId(viewBoardResponse.getId(), id);
            boolean isSave = boardLikeRepository.existsByBoardIdAndUserId(viewBoardResponse.getId(), id);
            List<BoardFiles> files = boardFileRepository.findAllByBoardId(viewBoardResponse.getId());
            files.forEach(file -> {
                switch (file.getFileType()) {
                    case IMAGE:
                        viewBoardResponse.getImg().add(awsS3Util.getURL(file.getFileName(), FileSize.IMAGE_512));
                        break;
                    case VIDEO:
                        viewBoardResponse.getImg().add(awsS3Util.getURL(file.getFileName(), FileSize.VIDEO));
                        viewBoardResponse.getImg().add(awsS3Util.getURL(file.getFileName(), FileSize.VIDEO_THUMBNAIL));
                        break;
                }
            });
//            UserResponse user = new UserResponse(userRepository.findById(boardList.get(i).getUserId()),1);
            viewBoardResponse.setIsLike(isLike);
            viewBoardResponse.setIsSave(isSave);
        });
        return list;
    }

    public List<ViewBoardResponse> list(Long userId, Long offset) {
        List<Boards> boardList = offset == null ? boardRepository.findAllByUserIdOrderByIdDesc(userId, Limit.of(20)) : boardRepository.findAllByUserIdAndIdLessThanOrderByIdDesc(userId, offset, Limit.of(20));
        List<ViewBoardResponse> list = boardList.stream().map(ViewBoardResponse::new).collect(Collectors.toList());

        list = getSubBoard(list, userId);

        for (int i = 0; i < boardList.size(); i++) {
            Bgms bgm = bgmRepository.findById(boardList.get(i).getBgmId()).orElseThrow(ErrorCode::throwDummyNotFound);
            ViewBgmResponse bgms = new ViewBgmResponse(bgm);
            list.get(i).setBgm(bgms);
        }

        return list;
    }

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


    @Transactional
    public Boards updateLike(Long id, int like) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setLikes(boards.getLikes() + like);
        boardRepository.save(boards);
        return boards;
    }

    @Transactional
    public Boards updateView(Long id, int view) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setViews(boards.getViews() + view);
        boardRepository.save(boards);
        return boards;
    }

    @Transactional
    public Boards updateComment(Long id, int comment) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setComments(boards.getComments() + comment);
        boardRepository.save(boards);
        return boards;
    }

    @Transactional
    public void updateShare(Long id, int share) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        boards.setShares(boards.getShares() + share);
        boardRepository.save(boards);
    }

    // 회원 탈퇴시 해당 회원이 작성한 게시글 삭제
    @Transactional
    public void deleteBoardsByUser(Long userId) {
        boardRepository.deleteByUserId(userId);
    }

    // 해당 회원의 총 게시글 수
    public int countBoardsByUserId(Long userId) {
        return boardRepository.countByUserId(userId);
    }

    public List<Boards> getSaveList(List<Long> boardIdList) {
        return boardRepository.findByIdIn(boardIdList);
    }

    public Boards getRandomAdvertisement() {
        Long id = advertisementRepository.findRandomApprovedAdvertisement().getBoardId();
        return boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
    }

    public boolean isLoginUser(Long id, Long userId) {
        Boards boards = boardRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        return boards.getUserId().equals(userId);
    }

//    public List<ViewBoardResponse> getTagList(Long id) {
//        List<Boards> list = boardRepository.findAllByUserId(id);
//        list.forEach(lists -> {
//
//        });
//
//    }
}
