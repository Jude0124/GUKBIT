package com.gukbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @GetMapping("/")
    public String indexMapping() {
        return "index";
    }

    @GetMapping("/signUp")
    public String signUpMapping() {
        return "/view/signUp";
    }

    @GetMapping("/login")
    public String loginMapping() {
        return "/view/login";
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
    
    @GetMapping("/academy")
    String academyMapping(Model model) {
        List<String> items = new ArrayList<>();
        items.add("강사진");
        items.add("커리큘럼");
        items.add("취업 연계");
        items.add("학원 내 문화");
        items.add("운영 및 시설");
        model.addAttribute("items", items);
        return "/view/academy";
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

    @GetMapping("/searchAcademy")
    String searchAcademy(){
        return "/view/searchAcademy";
    }

}
