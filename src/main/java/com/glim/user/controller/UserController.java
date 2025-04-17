package com.glim.user.controller;

import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.utils.SecurityUtil;
import com.glim.user.dto.request.AddDummyRequest;
import com.glim.user.dto.request.UpdateDummyRequest;
import com.glim.user.dto.response.ViewDummyResponse;
import com.glim.user.service.DummyService;
import com.glim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
