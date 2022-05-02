package com.gukbit.controller;


import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    //  회원가입
    @PostMapping("/processRegister")
    public String processRegistration(User user) {
        try {
            user.setRole("ROLE_USER");
            String rawPassword = user.getPassword();
            String encPassword = bCryptPasswordEncoder.encode(rawPassword); //비밀번호 암호화
            user.setPassword(encPassword);
            userService.joinUser(user);
            return "/view/register/register-success";
        } catch (DataIntegrityViolationException e) {
            System.out.println("email already exist");
            return "/view/register/register-fail";
        }
    }

    //  아이디 중복확인
    @PostMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestBody String id) throws Exception {
        System.out.println("UserController.idCheck");
        int count = 0;
        if (id != null) count = userService.idCheck(id);
        System.out.println("count = " + count);
        return count;
    }

    @GetMapping("/mypageAuth")
    public String myPageAuthGet(Model model){
        model.addAttribute("pwCheck", new PwCheck());
        return "view/mypage/mypage-auth";
    }

    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/mypage")
    public String joinMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model,
                             @ModelAttribute PwCheck pwCheck, BindingResult bindingResult) {

        System.out.println("loginUser = " + loginUser);
        if(pwCheck.getPassword().equals(""))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 비었습니다."));
        else if(!bCryptPasswordEncoder.matches(pwCheck.getPassword(),loginUser.getPassword()))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 다릅니다"));
        
        
        if (bindingResult.hasErrors()) {
            return "view/mypage/mypage-auth";
        }



        UpdateUserData updateUserData = new UpdateUserData(loginUser);
        userService.makeUpdateUser(updateUserData);
        model.addAttribute("updateUserData", updateUserData);

        return "/view/mypage/mypage";
    }

    @PostMapping("/mypage/update")
    public String updateMyPage(@ModelAttribute UpdateUserData updateUserData, BindingResult bindingResult, HttpServletRequest request) {
        userService.makeUpdateUser(updateUserData);
        userService.updateCheck(updateUserData, bindingResult,request);

        if (bindingResult.hasErrors()) {
            return "view/mypage/mypage";
        }
        return "redirect:/";
    }

    @GetMapping("/mypage/delete")
    public String deleteMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, HttpServletRequest request) {
        userService.deleteUser(loginUser);
        //성공 했다면
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @Getter @Setter //여기서만 쓸 클래스
    public static class PwCheck {
        String password;
    }
}