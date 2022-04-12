package com.gukbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    String indexMapping() {
        return "index";
    }
    
    @GetMapping("/signUp")
    String signUpMapping() {
        return "/view/signUp";
    }

    @GetMapping("/login")
    String loginMapping() {
        return "/view/login";
    }
}
