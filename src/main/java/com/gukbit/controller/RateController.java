package com.gukbit.controller;

import com.gukbit.domain.*;
import com.gukbit.dto.RateDto;
import com.gukbit.etc.UpdateUserData;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.service.AcademyService;
import com.gukbit.service.CourseService;
import com.gukbit.service.RateService;
import com.gukbit.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final CourseService courseService;

    public RateController(RateService rateService, AcademyService academyService,
                          UserService userService, CourseService courseService) {
        this.rateService = rateService;
        this.academyService = academyService;
        this.userService = userService;
        this.courseService = courseService;
    }

    /* 리뷰 작성 버튼 눌렀을 때 */
    @GetMapping("/review-input")
    public String reviewInputMapping(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("code") String code, Model model) {
        String userId = customUserDetails.getUsername();
        AuthUserData authUserData = rateService.getAuthUserData(userId);
        if (authUserData != null) {
            if (code.equals(authUserData.getAcademyCode())) {
                int session = authUserData.getSession();
                String courseId = authUserData.getCourseId();
                Course courseData = courseService.getCourseByIdAndSession(courseId, session);
                model.addAttribute("course", courseData);
                model.addAttribute("academycode", code);
            }
        }
        /* 학원 평점페이지 상단 정보 */
        Academy academyInfo = academyService.getAcademyInfo(code);
        model.addAttribute("academyInfo", academyInfo);

        return "view/academy/academy-review-input";

    }

    /* 리뷰 작성 완료 후 확인 버튼 눌렀을 때 DtoSet service로 */
    @PostMapping("/review-input")
    public String reviewInput(
            @RequestParam("code") String code,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            RateDto rateDto, Model model) {
        rateDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        rateDto.setRid(rateDto.getCCid() + customUserDetails.getUsername());  // 코스 id + user id
        rateDto.setUserId(customUserDetails.getUsername());
        rateService.saveReviewEval(rateDto, code, 1);
        rateService.saveReview(rateDto);

        return "redirect:/academy/review?code=" + code;
    }


    /* 마이페이지 과정평가 수정/삭제 버튼 */
    @GetMapping("/review-input/change")
    public String reviewInputChangeMapping(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("code") String code, Model model) {
        UpdateUserData updateUserData = new UpdateUserData(customUserDetails.getUser());
        userService.makeUpdateUser(updateUserData);
        model.addAttribute("updateUserData", updateUserData);

        Course courseForAcademy = courseService.getCourseByIdAndSession
                (updateUserData.getAuthUserData().getCourseId(), updateUserData.getAuthUserData().getSession());
        model.addAttribute("course", courseForAcademy);

        /* 학원 평점페이지 상단 정보 */
        Academy academyInfo = academyService.getAcademyInfo(code);
        model.addAttribute("academyInfo", academyInfo);

        return "view/academy/academy-review-input-rewrite";
    }


    /* 과정평가 수정/삭제 수정 버튼 Dto에서 처리하면 됨 */
    @PostMapping("/review-input/change/rewrite")
    public String reviewInputRewriteMapping(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            RateDto rateDto, Model model, @RequestParam("code") String code) {
        rateDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        rateDto.setUserId(customUserDetails.getUsername());
        rateDto.setRid(rateDto.getCCid() + customUserDetails.getUsername());
        rateService.saveReviewEval(rateDto, code, 1);
        rateService.saveReview(rateDto);
        return "redirect:/academy/review?code=" + code;
    }

    @PostMapping("/review-input/change/delete")
    public String reviewDeleteMapping(@RequestParam("rid") String rid, @RequestParam("code") String code) {
        rateService.deleteRate(rid);
        rateService.saveReviewEval(null, code, 0);
        return "redirect:/";
    }

}