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
    private String fileType;

    public BoardFiles toEntity(Long boardId, String fileName, String fileType) {
        return BoardFiles.builder()
                .boardId(boardId)
                .fileName(fileName)
                .fileType(fileType.equals("BASIC"))
                .build();
    }
}
