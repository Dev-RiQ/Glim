package com.glim.tag.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
public class TagRequest {
    private List<String> tags;
}
