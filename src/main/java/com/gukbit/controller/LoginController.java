package com.gukbit.controller;

import com.gukbit.etc.LoginData;
import com.gukbit.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    public static String prevPage = "/"; //이전페이지 저장정보

//    @GetMapping("/loginForm")
//    public String loginForm(Model model, HttpServletRequest request) {
//        //LoginData loginData = new LoginData();
//        model.addAttribute("loginData", new LoginData());
//
//        //로그인을 눌렀을 때 이전페이지 저장'
//        if(request.getHeader("Referer") != null){
//            prevPage = request.getHeader("Referer");
//        }
//
//        return "/view/user/loginForm";
//    }
//
//    @PostMapping("/login")
//    public String login(@ModelAttribute LoginData loginData,
//                        BindingResult bindingResult, HttpServletRequest request) {
//        User loginUser = loginService.login(loginData, bindingResult);
//        System.out.println("loginUser = " + loginUser);
//        if (bindingResult.hasErrors()) {
//            return "/view/user/loginForm";
//        }
//
//        //세션이 있으면 세션 반환, 없으면 신규 세션을 생성
//        HttpSession session = request.getSession();
//
//        //세션에 로그인 유저 정보 저장
//        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
//
//        //이전 페이지가 있다면 이전페이지로 이동
//        String[] list = prevPage.split("/");
//        //이전 페이지가 회원가입 페이지라면 홈으로 이동
//        if (list[list.length - 1].equals("processRegister")){
//            return "redirect:/";
//        }
//        return "redirect:" + prevPage;
//    }

    @GetMapping("/loginForm")
    public String loginForm(HttpServletRequest request, @RequestParam(value = "error", required = false)String error,
                            @RequestParam(value = "exception", required = false) String exception , Model model) {
        if(request.getHeader("Referer") != null){
            prevPage = request.getHeader("Referer");
        }
        System.out.println("error = " + error);
        System.out.println("exception = " + exception);
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

