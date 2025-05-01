package com.glim.common.awsS3.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileSize {

    IMAGE_128("_128x128.webp"),
    IMAGE_512("_512x512.webp"),

    VIDEO("_encoded.mp4"),
    VIDEO_THUMBNAIL("_thumbnail.webp"),

    AUDIO("_encoded.mp3");

    private final String typeAndSizeUri;
}
