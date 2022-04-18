package com.gukbit.controller;

import com.gukbit.dto.domain.Board;
import com.gukbit.dto.domain.User;
import com.gukbit.service.BoardService;
import com.gukbit.service.repository.UserRepository;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;

    private final BoardService boardService;

    @GetMapping("/")
    public String indexMapping(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false)User loginUser, Model model) {
        if(loginUser == null){
            return "index";
        }

        model.addAttribute("user", loginUser);

        return "index";
    }

    @GetMapping("/signUp")
    public String signUpMapping( Model model) {
        model.addAttribute("user", new User());
        return "/view/signUp";
    }

    @GetMapping("/findid")
    String findidMapping() {
        return "/view/findid";
    }

    @GetMapping("/findpw")
    String findpwMapping() {
        return "/view/findpw";
    }

    @GetMapping("/findpw-auth")
    String indpwAuthMapping() {
        return "/view/findpw-auth";
    }

    @GetMapping("/mypage")
    String myPageMapping() {
        return "/view/myPage";
    }

    @GetMapping("/review-input")
    String reviewInputMapping(){
        return "/view/review-input";
    }


    @GetMapping("/mypage-auth")
    String myPageAuthMapping() {
        return "/view/mypage-auth";
    }

    @GetMapping("/noticeList")
    String noticeList() {
        return "/view/noticeList";
    }

    @GetMapping("/notice")
    String notice() {
        return "/view/notice";
    }


