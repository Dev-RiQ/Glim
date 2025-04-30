package com.glim.borad.dto.response;

import com.glim.borad.domain.Boards;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.utils.CountUtil;
import com.glim.common.utils.DateTimeUtil;
import com.glim.user.dto.response.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Setter
public class ViewBoardResponse {
    private AwsS3Util awsS3Util;

    private final Long id;
    private final String location;
    private final List<String> img = List.of();
    private final String content;
    private final String view;
    private final String like;
    private final String comment;
    private final String share;
    private final String tagId;
    private final String createdAt;
    private final String updatedAt;
    private ViewBgmResponse bgm;
    private UserResponse user;
    private Boolean isLike;
    private Boolean isSave;
    private List<String> tags = List.of();

    public ViewBoardResponse(Boards board) {
        this.id = board.getId();
        this.location = board.getLocation();
//        for(int i = 0; i < request.size(); i++){
//            switch (board.getBoardType()) {
//                case BASIC:
//                    this.img.add(awsS3Util.getURL(request.get(i).getFileName(), FileSize.IMAGE_512));
//                    break;
//                case SHORTS:
//                    this.img.add(awsS3Util.getURL(request.get(i).getFileName(), FileSize.VIDEO));
//                    this.img.add(awsS3Util.getURL(request.get(i).getFileName(), FileSize.VIDEO_THUMBNAIL));
//                    break;
//                case ADVERTISEMENT:
//                    this.img.add(awsS3Util.getURL(request.get(i).getFileName(), FileSize.VIDEO));
//                    break;
//            }
//        }
        this.content = board.getContent();
        this.view = CountUtil.getCountString(board.getViews());
        this.like = CountUtil.getCountString(board.getLikes());
        this.comment = CountUtil.getCountString(board.getComments());
        this.share = CountUtil.getCountString(board.getShares());
        this.tagId = board.getTagUserIds();
        this.createdAt = DateTimeUtil.getDateTimeAgo(board.getCreatedAt());
        this.updatedAt = DateTimeUtil.getDateTimeAgo(board.getUpdatedAt());
    }

}
