package com.gukbit.controller;


import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  회원가입
    @PostMapping("/process_register")
    public String processRegistration(User user) {
    try {
        userService.joinUser(user);
        return "/view/register_success";
    } catch (DataIntegrityViolationException e) {
        System.out.println("email already exist");
        return "/view/register_fail";
    }
}

    //  아이디 중복확인
    @PostMapping("/idCheck")
    @ResponseBody
    public int IdCheck(@RequestBody String id) throws Exception {

        int count = 0;
        if (id != null) count = userService.idCheck(id);
        return count;
    }

    @PostMapping("/mypage")
    public String joinMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        UpdateUserData updateUserData = new UpdateUserData(loginUser);
        userService.makeUpdateUser(updateUserData);
        model.addAttribute("updateUserData", updateUserData);

        return "/view/myPage";
    }

    @PostMapping("/mypage/update")
    public String updateMyPage(@ModelAttribute UpdateUserData updateUserData, BindingResult bindingResult) {
        boolean check = userService.pwCheck(updateUserData, bindingResult);
        if (bindingResult.hasErrors()) {
            return "view/myPage";
        }


        if(check){
            User user = updateUserData.getUser();
            if(updateUserData.getChangePassword()!= null)
                user.setPassword(updateUserData.getChangePassword());
            userService.updateUser(user);
            return "redirect:/";
        }


        return "index";
    }

    @GetMapping("/mypage/delete")
    public String deleteMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, HttpServletRequest request){
        userService.deleteUser(loginUser);
        //성공 했다면
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}