package com.glim.common.awsS3.domain;

import lombok.*;

import java.io.File;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AwsS3 {
    private File uploadFile;
    private String filename;
}
