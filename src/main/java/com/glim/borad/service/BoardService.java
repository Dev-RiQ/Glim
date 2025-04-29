package com.glim.borad.service;

import com.glim.admin.repository.AdvertisementRepository;
import com.glim.borad.domain.Bgms;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.request.AddBoardFileRequest;
import com.glim.borad.dto.request.AddBoardRequest;
import com.glim.borad.dto.request.AddBoardTagRequest;
import com.glim.borad.dto.request.UpdateBoardRequest;
import com.glim.borad.dto.response.ViewBgmResponse;
import com.glim.borad.dto.response.ViewBoardResponse;
import com.glim.borad.repository.*;
import com.glim.common.exception.ErrorCode;
import com.glim.user.repository.UserRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final BoardTagRepository boardTagRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BgmRepository bgmRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @Transactional
    public void insert(AddBoardRequest request) {
        Boards board = boardRepository.save(new AddBoardRequest().toEntity(request));
        for(int i = 0; i < request.getImg().size(); i++){
            boardFileRepository.save(new AddBoardFileRequest().toEntity(board.getId(), request.getImg().get(i), String.valueOf(board.getBoardType())));
        }
        for(int i = 0; i < request.getTags().size(); i++){
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

    public List<ViewBoardResponse> list(Long userId, Long offset) {
        List<Boards> boardList = offset == null ?
                boardRepository.findAllByUserIdOrderByIdDesc(userId, Limit.of(10)) :
                boardRepository.findAllByUserIdAndIdLessThanOrderByIdDesc(userId, offset, Limit.of(10));
        List<ViewBoardResponse> list = boardList.stream().map(board -> new ViewBoardResponse(board)).collect(Collectors.toList());
        list.forEach(viewBoardResponse -> {
            boolean isLike = boardLikeRepository.existsByBoardIdAndUserId(viewBoardResponse.getId(), userId);
            boolean isSave = boardLikeRepository.existsByBoardIdAndUserId(viewBoardResponse.getId(), userId);
//            UserResponse user = new UserResponse(userRepository.findById(boardList.get(i).getUserId()),1);
            viewBoardResponse.setIsLike(isLike); // isLike 값을 세팅
            viewBoardResponse.setIsSave(isSave);
        });
        for (int i = 0; i < list.size(); i++) {
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
}
