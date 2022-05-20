package com.gukbit.controller;

import com.gukbit.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    public static String prevPage = "/"; //이전페이지 저장정보


    @GetMapping("/loginForm")
    public String loginForm(HttpServletRequest request, @RequestParam(value = "error", required = false)String error,
                            @RequestParam(value = "exception", required = false) String exception , Model model) {
        if(request.getHeader("Referer") != null){
            prevPage = request.getHeader("Referer");
        }
        model.addAttribute("error", error);
        model.addAttribute("exception",exception);
        return "view/user/loginForm";
    }

    @PostMapping("/login")
    public String login(){
        return "view/user/loginForm";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

}

