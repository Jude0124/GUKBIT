package com.gukbit.controller;

import com.gukbit.domain.*;
import com.gukbit.dto.AcademyDto;
import com.gukbit.service.AcademyService;
import com.gukbit.service.CourseService;
import com.gukbit.service.RateService;
import com.gukbit.session.SessionConst;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/academy")
public class AcademyController {

    private final AcademyService academyService;
    private final RateService rateService;
    private final CourseService courseService;

    public AcademyController(AcademyService academyService, RateService rateService, CourseService courseService) {
        this.academyService = academyService;
        this.rateService = rateService;
        this.courseService =  courseService;
    }


    //리뷰 탭
    @GetMapping("/review")
    String academyMapping(@RequestParam("code") String code,
                          @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                          @Qualifier("reviewed") Pageable pageable1, @Qualifier("expected") Pageable pageable2, Model model) {
        System.out.println("pageable1 = " + pageable1);
//   @GetMapping({"", "/", })
//   String academyMapping(@RequestParam ("code") String code,
//       @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
//       Model model) {

        /* 평가 리뷰출력 페이지 데이터 */
        List<String> items = new ArrayList<>();
        items.add("강사진");
        items.add("커리큘럼");
        items.add("취업 연계");
        items.add("학원 내 문화");
        items.add("운영 및 시설");
        model.addAttribute("items", items);


        /* 학원 정보 출력 */
        Academy academy_info = academyService.getAcademyInfo(code);
        model.addAttribute("academy_info", academy_info);

        /* 해당 학원의 과정 정보 출력 */

        List<Course> course_list =  courseService.getCourseList(code);
        double[] evalAll = academyService.reviewCourseAverage(course_list);
        Page<Rate> page1 = academyService.reviewCoursePageList(course_list,pageable1);
        Page<Course> page2 = academyService.expectedCoursePageList(code, pageable2);


        model.addAttribute("reviewCoursePageList", page1);
        model.addAttribute("expectedCoursePageList", page2);
        model.addAttribute("link1", "academy/review?code="+code);
        model.addAttribute("link2", "academy/expected?code="+code);
        model.addAttribute("expectedSelect",false);




        /* 로그인 유저 관련 정보 전달 */
        try {
            String userId = loginUser.getUserId();
            AuthUserData authUserData = rateService.getAuthUserData(userId);
            model.addAttribute("authUserData", authUserData);

        } catch (NullPointerException e) {
            AuthUserData authUserData = null;
            model.addAttribute("authUserData", authUserData);
        }

        System.out.println("page1 = " + page1);
        return "/view/academy";
    }

    //모집중인 과정 탭
    @GetMapping("/expected")
    String expectedMapping(@RequestParam("code") String code, @Qualifier("reviewed") Pageable pageable1, @Qualifier("expected") Pageable pageable2, Model model) {
        List<String> items = new ArrayList<>();
        items.add("강사진");
        items.add("커리큘럼");
        items.add("취업 연계");
        items.add("학원 내 문화");
        items.add("운영 및 시설");
        model.addAttribute("items", items);


        /* 학원 정보 출력 */
        Academy academy_info = academyService.getAcademyInfo(code);
        model.addAttribute("academy_info", academy_info);

        /* 해당 학원의 과정 정보 출력 */

        List<Course> course_list =  courseService.getCourseList(code);
        Page<Rate> page1 = academyService.reviewCoursePageList(course_list,pageable1);

        Page<Course> page2 = academyService.expectedCoursePageList(code, pageable2);
        model.addAttribute("reviewCoursePageList", page1);
        model.addAttribute("expectedCoursePageList", page2);

        model.addAttribute("link1", "academy/review?code=" + code);
        model.addAttribute("link2", "academy/expected?code=" + code);
        model.addAttribute("expectedSelect", true);
        return "/view/academy";
    }

    @GetMapping("/search")
    public String searchAcademy(@RequestParam(value = "keyword") String keyword, Model model) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(keyword);
        model.addAttribute("academyList", academyDtoList);
        model.addAttribute("keyword", keyword);
        return "/view/searchAcademy";
    }
}
