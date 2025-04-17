package com.glim.borad.dto.request;

import com.glim.borad.domain.BoardFiles;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddBoardFileRequest {

    private Long boardId;
    private String fileName;
    private Boolean fileType;

    public BoardFiles toEntity(AddBoardFileRequest request) {
        return BoardFiles.builder()
                .boardId(request.getBoardId())
                .fileName(request.getFileName())
                .fileType(request.getFileType())
                .build();
    }
}
