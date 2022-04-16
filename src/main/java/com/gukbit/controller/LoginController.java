package com.gukbit.controller;

import com.gukbit.domain.User;
import com.gukbit.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginMapping() {
        return "view/login";
    }

    @PostMapping("/login.do")
    public String loginConfirm(@RequestParam String id, @RequestParam String password, HttpServletResponse response) {
        User loginUser = loginService.login(id, password);

        if (loginUser == null)
            return "redirect:/login"; //bindingResult 처리해주기

        //시간정보를 주지 않았기 때문에 세션 쿠키
        Cookie idCookie = new Cookie("loginId", loginUser.getId());
        idCookie.setPath("/");
        response.addCookie(idCookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        System.out.println("response = " + response);
        expireCookie(response, "loginId");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

