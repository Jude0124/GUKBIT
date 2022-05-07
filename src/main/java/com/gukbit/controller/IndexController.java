package com.gukbit.controller;


import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.domain.Board;
import com.gukbit.domain.Course;
import com.gukbit.domain.DivisionS;
import com.gukbit.domain.User;
import com.gukbit.service.BoardService;
import com.gukbit.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BoardService boardService;
    @Autowired
    private IndexService indexservice;

    @RequestMapping(value = "/indexCard", method = {RequestMethod.POST})
    @ResponseBody
    public List<Course> indexSlideData(@RequestParam(value = "Tag") String tag, @RequestParam(value = "Local") String local) {
        return indexservice.getCodeAcademy(tag, local);
    }

    @GetMapping("/")
    public String indexMapping(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable, Model model) {
        List<DivisionS> DivisionSs = indexservice.selectSlideMenu();
        model.addAttribute("sideMenuList", DivisionSs);

        Page<Board> p1 = boardService.findBoardSampleNew(pageable);
        model.addAttribute("boardListNew", p1);


        Page<Board> p2 = boardService.findBoardSampleBest(pageable);
        model.addAttribute("boardListBest", p2);

        if (customUserDetails == null) {
            return "index";
        }

        model.addAttribute("user", customUserDetails);
        if(customUserDetails != null)
            System.out.println("principalDetails.getUsername() = " + customUserDetails.getUsername());
        return "index";
    }


    @GetMapping("/signUp")
    public String signUpMapping(Model model) {
        model.addAttribute("user", new User());
        return "/view/register/sign-up";
    }

    @GetMapping("/review-input")
    String reviewInputMapping() {
        return "view/academy/academy-review-input";
    }

    @GetMapping("/mypage")
    String myPageMapping() {
        return "/view/mapage/mypage";
    }


    @GetMapping("/mypage-auth")
    String myPageAuthMapping() {
        return "/view/mypage/mypage-auth";
    }

    @GetMapping("/notice")
    String notice() {
        return "/view/notice-list";
    }

    @GetMapping("/wordcloud")
    String wordCloud() {
        return "/view/word-cloud";
    }
}

