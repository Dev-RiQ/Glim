package com.glim.stories.service;

import com.glim.borad.domain.BoardType;
import com.glim.borad.domain.Boards;
import com.glim.borad.dto.response.ViewMyPageBoardResponse;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.ErrorCode;
import com.glim.stories.domain.Stories;
import com.glim.stories.dto.request.AddStoryRequest;
import com.glim.stories.dto.response.ViewMyPageStoryResponse;
import com.glim.stories.dto.response.ViewStoryResponse;
import com.glim.stories.repository.StoryLikeRepository;
import com.glim.stories.repository.StoryRepository;
import com.glim.stories.repository.StoryViewRepository;
import com.glim.user.dto.response.ViewBoardUserResponse;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final StoryViewRepository storyViewRepository;
    private final UserRepository userRepository;
    private final AwsS3Util awsS3Util;

    @Transactional
    public void insert(AddStoryRequest request) {
        storyRepository.save(new AddStoryRequest().toEntity(request));
    }

    @Transactional
    public void delete(Long id) {
        storyRepository.deleteById(id);
    }

    @Transactional
    public Stories updateLike(Long id, int like) {
        Stories stories = storyRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        stories.setLikes(stories.getLikes() + like);
        storyRepository.save(stories);
        return stories;
    }

    @Transactional
    public Stories updateView(Long id, int view) {
        Stories stories = storyRepository.findById(id).orElseThrow(ErrorCode::throwDummyNotFound);
        stories.setViews(stories.getViews() + view);
        storyRepository.save(stories);
        return stories;
    }

    public Boolean isStory(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusHours(24);

        return storyRepository.existsByUserIdAndCreatedAtBetween(userId, yesterday, now);
    }

    @Transactional
    public void deleteStoriesByUser(Long userId) {
        storyRepository.deleteByUserId(userId);
    }


    public Stories getStory(Long storyId) {
        return storyRepository.findById(storyId).orElseThrow(ErrorCode::throwDummyNotFound);
    }

    public List<ViewStoryResponse> getStoryList(Long userId, Long loginId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusHours(24);
        ViewBoardUserResponse user = new ViewBoardUserResponse(userRepository.findById(userId).orElseThrow(ErrorCode::throwDummyNotFound));
        user.setIsStory(true);
        user.setIsMine(userId.equals(loginId));
        user.setImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
        List<Stories> stories = storyRepository.findByUserIdAndCreatedAtBetween(userId, yesterday, now);
        List<ViewStoryResponse> responses = stories.stream().map(ViewStoryResponse::new).collect(Collectors.toList());
        responses.forEach(viewStoryResponse -> {
            boolean isLike = storyLikeRepository.existsByStoryIdAndUserId(viewStoryResponse.getId(), userId);
            viewStoryResponse.setIsLike(isLike);
            viewStoryResponse.setUser(user);
            viewStoryResponse.setImg(awsS3Util.getURL(viewStoryResponse.getImg(), FileSize.IMAGE_512));
        });
        return responses;
    }

    public List<ViewMyPageStoryResponse> myPageStoryList(Long userId, Long offset) {
        List<Stories> storyList = offset == null ? storyRepository.findAllByUserIdOrderByIdDesc(userId, Limit.of(20))
                : storyRepository.findAllByUserIdAndIdLessThanOrderByIdDesc(userId, offset, Limit.of(20));
        storyList.forEach(story -> {
            story.setFileName(awsS3Util.getURL(story.getFileName(), FileSize.IMAGE_128));
        });
        return storyList.stream().map(ViewMyPageStoryResponse::new).collect(Collectors.toList());
    }
}


