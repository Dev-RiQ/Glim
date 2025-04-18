package com.glim.common.awsS3.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    IMAGE("images"),
    VIDEO("videos"),
    AUDIO("audios");

    private final String type;
}
