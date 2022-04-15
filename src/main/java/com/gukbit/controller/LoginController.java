package com.gukbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping({"", "/"})
    public String loginMapping() {
        return "/view/login";
    }

    @PostMapping
    public String loginConfirm(){

        return "/view/index";
    }
}
