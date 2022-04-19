package com.gukbit.controller;


import com.gukbit.domain.Course;
import com.gukbit.domain.Division_S;
import com.gukbit.service.indexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.gukbit.domain.User;
import com.gukbit.repository.UserRepository;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;


@Controller
@RequiredArgsConstructor
public class IndexController {

    @Autowired
    private indexService indexservice;

    @RequestMapping ( value = "/indexCard", method = {RequestMethod.POST})
    @ResponseBody
    public List<Course> indexSlideData(@RequestParam(value = "Tag") String tag, @RequestParam(value ="Local") String local, Model model) {

        return indexservice.getCodeAcademy(tag, local);
    }


        private final UserRepository userRepository;

        @GetMapping("/")
        public String indexMapping(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
            List<Division_S> DividsionSs = indexservice.selectSlideMenu();
            model.addAttribute("sideMenuList", DividsionSs);

            if(loginUser == null){
                return "index";
            }

            model.addAttribute("user", loginUser);
            return "index";
        }


        @GetMapping("/signUp")
        public String signUpMapping(Model model) {
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



    }
