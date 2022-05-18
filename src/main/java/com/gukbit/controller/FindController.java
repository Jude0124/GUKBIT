package com.gukbit.controller;


import com.gukbit.domain.PreAuthUserData;
import com.gukbit.domain.UploadFile;
import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.ImageService;
import com.gukbit.service.MailService;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/find")
public class FindController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/findId")
    public String findId() {
        return "view/user/find-id";
    }

    @PostMapping("/findIdByTel")
    public String findIdByTel(@RequestParam("tel") String tel, Model model) {
        String message = userService.findIdByTel(tel);
        model.addAttribute("message", message);
        return "view/user/find-id-result";
    }

    @PostMapping("/findIdByEmail")
    public String findIdByEmail(@RequestParam("email") String email, Model model) {
        String message = userService.findIdByEmail(email);
        model.addAttribute("message", message);
        return "view/user/find-id-result";
    }

    @GetMapping("/findPw")
    public String findPwAuth() {
        return "view/user/find-pw";
    }

    @PostMapping("/findPwId")
    public String findPwId(@RequestParam("id") String id, Model model) {
        int count = 0;
        if (id != null) {
            count = userService.idCheck(id);
        }

        if (count != 0) {
            model.addAttribute("userId", id);
            return ("view/user/find-pw-auth");
        } else {
            model.addAttribute("message", "회원 정보를 찾을 수 없습니다.");
            return ("view/user/find-pw-fail");
        }
    }

    @PostMapping("/emailGetCode")
    @ResponseBody
    public String emailGetCode(@RequestParam("id") String id, @RequestParam("email") String email, Model model) {
        if (userService.checkEmail(id, email) == 1) {
            String code = mailService.sendEmailMessage(email);
            model.addAttribute("code", code);
            return code;
        } else {
            return "회원정보가 일치하지 않습니다.";
        }
    }

    @PostMapping("/telGetCode")
    @ResponseBody
    public String telGetCode(@RequestParam("id") String id, @RequestParam("tel") String tel, Model model) {
        if (userService.checkTel(id, tel) == 1) {
            String code = mailService.sendTelMessage(tel);
            model.addAttribute("code", code);
            return code;
        } else {
            return "회원정보가 일치하지 않습니다.";
        }
    }

    @PostMapping("/findPwEmail")
    public String findPwEmail(@RequestParam("code") String code, Model model) {
        String result = mailService.getUserIdByEmailCode(code);
        if (result.equals("fail")) {
            model.addAttribute("message", "인증코드를 다시 한 번 확인해주세요.");
            return ("view/user/find-pw-fail");
        } else {
            model.addAttribute("userId", result);
            return ("view/user/find-pw-success");
        }
    }

    @PostMapping("/findPwTel")
    public String findPwTel(@RequestParam("code") String code, Model model) {
        String result = mailService.getUserIdByTelCode(code);
        if (result.equals("fail")) {
            model.addAttribute("message", "인증코드를 다시 한 번 확인해주세요.");
            return ("view/user/find-pw-fail");
        } else {
            model.addAttribute("userId", result);
            return ("view/user/find-pw-success");
        }
    }

    @PostMapping("/changePw")
    public String changePw(@RequestParam("id") String id, @RequestParam("password") String password) {
        password = bCryptPasswordEncoder.encode(password);
        userService.changePassword(id, password);
        return "redirect:/";
    }

}