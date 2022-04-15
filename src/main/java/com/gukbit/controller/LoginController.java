package com.gukbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping({"", "/"})
    public String loginMapping() {
        return "view/login";
    }

    @PostMapping("/login.do")
    public String loginConfirm(@RequestParam String id, @RequestParam String password){
        System.out.println("id = " + id);
        System.out.println("password = " + password);
        return "redirect:/";
    }
}

