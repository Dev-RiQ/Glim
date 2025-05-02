package com.glim.borad.controller;

import com.glim.borad.service.BoardTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/boardTag")
public class BoardTagController {

    private final BoardTagService boardTagService;


}
