package com.glim.user.controller;

import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")
    public SecurityUserDto getCurrentUser() {
        return SecurityUtil.getUser();
    }





}
