package com.gukbit.controller;


import com.gukbit.domain.User;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.service.MailService;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    //  회원가입
    @PostMapping("/processRegister")
    public String processRegistration(User user) {
        try {
            userService.joinUser(user);
            System.out.println("UserController.processRegistration");
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
        int count = 0;
        if (id != null) count = userService.idCheck(id);
        return count;
    }

    @GetMapping("/mypageAuth")
    public String myPageAuthGet(Model model){
        model.addAttribute("pwCheck", new PwCheck());
        return "view/mypage/mypage-auth";
    }

    @PostMapping("/mypage")
    public String joinMyPage(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model,
                             @ModelAttribute PwCheck pwCheck, BindingResult bindingResult) {


        if(pwCheck.getPassword().equals(""))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 비었습니다."));
        else if(!pwCheck.getPassword().equals(loginUser.getPassword()))
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

    /* mypage 인증하기 버튼, 팝업창으로 연결 */
    @GetMapping("/mypage/ocr")
    public String ocrPopup(){
        return "view/mypage/mypage-ocr";
    }

    /* mypage OCR 사진 업로드 */
    @ResponseBody
    @PostMapping("/mypage/ocr")
    public Map<String, String> testOcr(@RequestParam("ocrFile") MultipartFile ocrFile) {
        Map<String, String> ocrInfo = userService.ocrService(ocrFile);
//        System.out.println("controller: "+ocrInfo);
        return ocrInfo;

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
        if (mailService.getUserIdByEmailCode(code).equals("fail")) {
            model.addAttribute("message", "인증코드를 다시 한 번 확인해주세요.");
            return ("view/user/find-pw-fail");
        } else {
            model.addAttribute("userId", mailService.getUserIdByEmailCode(code));
            return ("view/user/find-pw-success");
        }
    }

    @PostMapping("/findPwTel")
    public String findPwTel(@RequestParam("code") String code, Model model) {
        if (mailService.getUserIdByTelCode(code).equals("fail")) {
            model.addAttribute("message", "인증코드를 다시 한 번 확인해주세요.");
            return ("view/user/find-pw-fail");
        } else {
            model.addAttribute("userId", mailService.getUserIdByTelCode(code));
            return ("view/user/find-pw-success");
        }
    }

    @PostMapping("/changePw")
    public String changePw(@RequestParam("id") String id, @RequestParam("password") String password) {
        userService.changePassword(id, password);
        return "redirect:/";

    }

}