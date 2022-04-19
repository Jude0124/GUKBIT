package com.gukbit.controller;

import com.gukbit.domain.Course;
import com.gukbit.domain.User;
import com.gukbit.dto.RateDto;
import com.gukbit.service.RateService;
import com.gukbit.service.indexService;
import com.gukbit.session.SessionConst;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/academy/rate")
public class RateController {

  private final RateService rateService;

  public RateController(RateService rateService) {
    this.rateService = rateService;
  }

  @GetMapping("/review-input")
  public String reviewInputMapping(Model model) {
    // 해당 학원이 가진 course 데이터 뿌리는 로직

    // 학원 코드 넘겨받는 코드
    // --------------------------------------------------------
    System.out.println("controller 도착");
    // course 데이터 뿌리는 로직 course find by field 사용(학원코드로 접근), coursename가져오기
//    String field_s = "학원코드";
    String academyCode = "500020010894";
    List<Course> courseListForAcademy= rateService.getCoursesByAcademyCode(academyCode);
    System.out.println("RateController: courseListForAcademy"+courseListForAcademy);

    model.addAttribute("courseList", courseListForAcademy);
    return "/view/review-input";
  }

  @PostMapping("/review-input")
  public String reviewInput(
      @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
      RateDto rateDto) {


    // rid 가져오는 로직
    rateDto.setRid("testRid");  // 코스 id + user id
    // 코스 id 가져오는 로직
//    rateDto.setC_cid("testaa011");  // 코스 id + session(course cid)


    rateDto.setUserId(loginUser.getUserId());
    System.out.println(rateDto);
    rateService.saveReview(rateDto);
    return "redirect:/academy";
  }
}
