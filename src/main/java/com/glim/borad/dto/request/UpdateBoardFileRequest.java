package com.glim.borad.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateBoardFileRequest {

    private String fileName;
    private Boolean fileType;
}
