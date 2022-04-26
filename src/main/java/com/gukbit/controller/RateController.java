package com.gukbit.controller;

import com.gukbit.domain.Academy;
import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.User;
import com.gukbit.dto.RateDto;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.service.AcademyService;
import com.gukbit.service.RateService;
import com.gukbit.service.UserService;
import com.gukbit.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/academy/rate")
public class RateController {

    private final RateService rateService;
    private final AcademyService academyService;
    private final UserService userService;

    public RateController(RateService rateService, AcademyService academyService,
                          UserService userService) {
        this.rateService = rateService;
        this.academyService = academyService;
        this.userService = userService;
    }

    /* 리뷰 작성 버튼 눌렀을 때 */
    @GetMapping("/review-input")
    public String reviewInputMapping(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
            @RequestParam("code") String code, Model model) {
        String userId = loginUser.getUserId();
        AuthUserData authUserData = rateService.getAuthUserData(userId);
        if (authUserData != null) {
            if (code.equals(authUserData.getAcademyCode())) {
                int session = authUserData.getSession();
                String courseId = authUserData.getCourseId();
                Course courseForAcademy = rateService.getCourseByCourseidAndSession(courseId, session);
                model.addAttribute("course", courseForAcademy);
                model.addAttribute("academycode", code);
            } else {
            }
        } else {
            authUserData = null;
        }
        /* 학원 평점페이지 상단 정보 */
        Academy academyInfo = academyService.getAcademyInfo(code);
        model.addAttribute("academyInfo", academyInfo);

        return "/view/academy-review-input";

    }

    /* 리뷰 작성 완료 후 확인 버튼 눌렀을 때 */
    @PostMapping("/review-input")
    public String reviewInput(
            @RequestParam("code") String code,
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
            RateDto rateDto, Model model) {
        rateDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        rateDto.setRid(rateDto.getCCid() + loginUser.getUserId());  // 코스 id + user id
        rateDto.setUserId(loginUser.getUserId());
        rateService.saveReviewEval(rateDto, code, 1);
        rateService.saveReview(rateDto);

        return "redirect:/academy/review?code=" + code;
    }


    /* 마이페이지 과정평가 수정/삭제 버튼 */
    @GetMapping("/review-input/change")
    public String reviewInputChangeMapping(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
            @RequestParam("code") String code, Model model) {
        UpdateUserData updateUserData = new UpdateUserData(loginUser);
        userService.makeUpdateUser(updateUserData);
        model.addAttribute("updateUserData", updateUserData);

        Course courseForAcademy = rateService.getCourseByCourseidAndSession
                (updateUserData.getAuthUserData().getCourseId(), updateUserData.getAuthUserData().getSession());
        model.addAttribute("course", courseForAcademy);

        /* 학원 평점페이지 상단 정보 */
        Academy academyInfo = academyService.getAcademyInfo(code);
        model.addAttribute("academyInfo", academyInfo);
        model.addAttribute("academyInfo", academyInfo);

        return "/view/academy-review-input-rewrite";
    }


    /* 과정평가 수정/삭제 수정 버튼 */
    @PostMapping("/review-input/change/rewrite")
    public String reviewInputRewriteMapping(
            @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
            RateDto rateDto, Model model, @RequestParam("code") String code) {
        rateDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        rateDto.setUserId(loginUser.getUserId());
        rateDto.setRid(rateDto.getCCid() + loginUser.getUserId());
        rateService.saveReviewEval(rateDto, code, 1);
        rateService.saveReview(rateDto);
        return "redirect:/";
    }

    @PostMapping("/review-input/change/delete")
    public String reviewDeleteMapping(@RequestParam("rid") String rid, @RequestParam("code") String code) {
        rateService.deleteRate(rid);
        rateService.saveReviewEval(null, code, 0);
        return "redirect:/";
    }

}