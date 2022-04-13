package com.gukbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/community")
    String communityMapping() {
        return "/view/communityboard";
    }

    @GetMapping("/mypage")
    String myPageMapping() {
        return "/view/myPage";
    }

    @GetMapping("/academy")
    String academyMapping() {
        return "/view/academy";
    }

     @GetMapping("/mypage-auth")
    String myPageAuthMapping() {
        return "/view/mypage-auth";
    }
  
    @GetMapping("/searchAcademy")
    String searchAcademyMapping() {
        return "/view/searchAcademy";
    }

}


