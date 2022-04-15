package com.gukbit.controller;

import com.gukbit.service.MemberService;
import com.gukbit.model.MemberModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    MemberService service;

    @GetMapping("signUp.do")
    public void setInsert() throws Exception{};

    @PostMapping("signUp.do")
    public String setInsert(MemberModel memberVO) throws Exception{
        service.setInsert(memberVO);
        return "redirect:/view/login";
    }

}