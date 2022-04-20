package com.gukbit.controller;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.User;
import com.gukbit.dto.RateDto;
import com.gukbit.service.RateService;
import com.gukbit.session.SessionConst;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/academy/rate")
public class RateController {

  private final RateService rateService;

  public RateController(RateService rateService) {
    this.rateService = rateService;
  }

  @GetMapping("/review-input")
  public String reviewInputMapping(
      @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
      @RequestParam("code") String code,
      Model model, RedirectAttributes re
      ) {
/*    String academyCode = code;
    List<Course> courseListForAcademy = rateService.getCoursesByAcademyCode(academyCode);*/

    String userId = loginUser.getUserId();
    // userid로 authUserDataRepository 접근(findByUserId), academycode/courseid/session 체크
    AuthUserData authUserData = rateService.getAuthUserData(userId);
    System.out.println("controller authuserdata: "+authUserData);
    if(authUserData!=null){
      // 있으면 courseid/session 가져와서 바로 띄워버리기
      int session = authUserData.getSession();
      String courseId = authUserData.getCourseId();

      Course courseForAcademy = rateService.getCourseByCourseidAndSession(courseId, session);

//    model.addAttribute("courseList", courseListForAcademy);
      model.addAttribute("course", courseForAcademy);
      model.addAttribute("academycode", code);
    }
    else {
      // 없으면 튕겨내고
      authUserData = null;
    }

    return "/view/academy_review-input";

  }

  @PostMapping("/review-input")
  public String reviewInput(
      @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
      RateDto rateDto) {
    rateDto.setRid(rateDto.getC_cid() + loginUser.getUserId());  // 코스 id + user id
    rateDto.setUserId(loginUser.getUserId());
    rateService.saveReview(rateDto);
    return "redirect:/academy?code=500020033277";
  }
}
