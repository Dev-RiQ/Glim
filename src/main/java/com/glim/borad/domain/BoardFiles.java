package com.glim.borad.domain;

import com.glim.borad.dto.request.UpdateBoardFileRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_files")
@ToString()
public class BoardFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_file_id", nullable = false)
    private Long boardFileId;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Builder
    public BoardFiles(Long boardId, String fileName, Boolean fileType) {
        this.boardId = boardId;
        this.fileName = fileName;
        this.fileType = fileType ? FileType.IMAGE : FileType.VIDEO;
    }

    public void update(UpdateBoardFileRequest request) {
        this.fileName = request.getFileName();
        this.fileType = request.getFileType() ? FileType.IMAGE : FileType.VIDEO;
    }
}
