package com.glim.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/sign-up")
    public String signupPage() { // 아직 구현안함 ~
        return "user/join";
    }
}
