package com.gukbit.controller;


import com.gukbit.domain.Board;
import com.gukbit.domain.Course;
import com.gukbit.domain.Division_S;
import com.gukbit.domain.User;
import com.gukbit.service.BoardService;
import com.gukbit.service.indexService;
import com.gukbit.session.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BoardService boardService;
    @Autowired
    private indexService indexservice;

    @RequestMapping(value = "/indexCard", method = {RequestMethod.POST})
    @ResponseBody
    public List<Course> indexSlideData(@RequestParam(value = "Tag") String tag, @RequestParam(value = "Local") String local) {
        return indexservice.getCodeAcademy(tag, local);
    }

    @GetMapping("/")
    public String indexMapping(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Pageable pageable, Model model) {
        List<Division_S> DivisionSs = indexservice.selectSlideMenu();
        model.addAttribute("sideMenuList", DivisionSs);

        Page<Board> p1 = boardService.findBoardSampleNew(pageable);
        model.addAttribute("boardListNew", p1);


        Page<Board> p2 = boardService.findBoardSampleBest(pageable);
        model.addAttribute("boardListBest", p2);

        if (loginUser == null) {
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

    @GetMapping("/review-input")
    String reviewInputMapping() {
        return "academy_review-input";
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

    @GetMapping("/notice")
    String notice() {
        return "/view/noticeList";
    }

    @GetMapping("/wordcloud")
    String wordCloud() {
        return "/view/wordCloud";
    }
}

