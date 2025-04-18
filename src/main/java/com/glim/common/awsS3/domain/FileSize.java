package com.glim.common.awsS3.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileSize {

    IMAGE_128("_128x128.webp"),
    IMAGE_256("_256x256.webp"),
    IMAGE_512("_512x512.webp"),
    IMAGE_1024("_1024x1024.webp");

    private final String typeAndSizeUri;
}
