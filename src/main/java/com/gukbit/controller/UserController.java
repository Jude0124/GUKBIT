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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final ImageService imageService;

    @GetMapping("/mypageProfile")
    public String myPageProfile(Model model, Pageable pageable){
        return "view/mypage/mypage-profile";
    }

    @GetMapping("/mypageAuth")
    public String myPageAuthGet(Model model){
        model.addAttribute("pwCheck", new PwCheck());
        return "view/mypage/mypage-auth";
    }

    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/mypage")
    public String joinMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model,
                             @ModelAttribute PwCheck pwCheck, BindingResult bindingResult) {

        if(pwCheck.getPassword().equals(""))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 비었습니다."));
        else if(!bCryptPasswordEncoder.matches(pwCheck.getPassword(),customUserDetails.getPassword()))
            bindingResult.addError(new FieldError("pwCheck","password","비밀번호가 다릅니다"));


        if (bindingResult.hasErrors()) {
            return "view/mypage/mypage-auth";
        }

        UpdateUserData updateUserData = new UpdateUserData(customUserDetails.getUser());
        userService.makeUpdateUser(updateUserData);
        model.addAttribute("updateUserData", updateUserData);
        model.addAttribute("userData",userService.checkUser(customUserDetails) );
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

    @GetMapping("/mypage/ocr/check")
    public String ocrCheckPopup(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        model.addAttribute("preAuthUserData", userService.getPreAuthUserDataByUserId(customUserDetails.getUser().getUserId()));
        return "view/mypage/mypage-ocr-check";
    }

    /* mypage OCR 사진 업로드 */
    @ResponseBody
    @PostMapping("/mypage/ocr")
    public Map<String, String> ocrService(@RequestParam("ocrFile") MultipartFile ocrFile) {
        Map<String, String> ocrInfo = userService.ocrService(ocrFile);
//        System.out.println("controller: "+ocrInfo);
        return ocrInfo;
    }

    @PostMapping("/mypage/savePreAuthUser")
    @ResponseBody
    public String savePreAuthUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
        PreAuthUserData preAuthUserData, @RequestPart("ocrFile") MultipartFile ocrFile) throws Exception {
        System.out.println(ocrFile);
        System.out.println("controller pAUD: "+preAuthUserData);

        String rootLocation = "src/main/resources/static/images/mypage/preAuthUser";
        UploadFile saveFile = imageService.store(rootLocation,ocrFile);

        if(userService.setPreAuthUser(saveFile, customUserDetails, preAuthUserData)){
            /* 과정 재인증 요청 유저의 경우 확인하는 로직 필요 */
            userService.checkUserRate(customUserDetails.getUsername());
            return "true";
        } else {
            return "false";
        }


    }

}