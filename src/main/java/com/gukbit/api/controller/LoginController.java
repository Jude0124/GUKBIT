package com.gukbit.api.controller;

import com.gukbit.domain.User;
import com.gukbit.etc.LoginData;
import com.gukbit.service.LoginService;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginMapping(Model model) {
        LoginData loginData = new LoginData();
        model.addAttribute("loginData", loginData);
        return "view/Login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginData loginData, BindingResult bindingResult, HttpServletRequest request) {
        User loginUser = loginService.login(loginData, bindingResult);

        if (bindingResult.hasErrors()) {
            return "view/Login";
        }

        //세션이 있으면 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        //세션에 로그인 유저 정보 저장
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}

