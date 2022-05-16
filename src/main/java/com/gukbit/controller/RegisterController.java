package com.gukbit.controller;


import com.gukbit.domain.User;
import com.gukbit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    //  회원가입 => 암호화 로직은 joinUser안으로..
    @PostMapping("/processRegister")
    public String processRegistration(User user) {
        try {
            user.setRole("ROLE_USER");
            String rawPassword = user.getPassword();
            String encPassword = bCryptPasswordEncoder.encode(rawPassword); //비밀번호 암호화
            user.setPassword(encPassword);
            user.setLockUser(false);
            user.setAuth(0);
            userService.joinUser(user);
            return "view/register/register-success";
        } catch (DataIntegrityViolationException e) {
            System.out.println("email already exist");
            return "view/register/register-fail";
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

}