package com.gukbit.controller;

import com.gukbit.domain.Academy;
import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.User;
import com.gukbit.dto.RateDto;
import com.gukbit.service.AcademyService;
import com.gukbit.service.RateService;
import com.gukbit.session.SessionConst;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/academy/rate")
public class RateController {

  private final RateService rateService;
  private final AcademyService academyService;

  public RateController(RateService rateService, AcademyService academyService) {
    this.rateService = rateService;
    this.academyService = academyService;
  }

  /* 리뷰 작성 버튼 눌렀을 때 */
  @GetMapping("/review-input")
  public String reviewInputMapping(
      @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
      @RequestParam("code") String code, Model model) {
    String userId = loginUser.getUserId();  // session userid 가져옴
    AuthUserData authUserData = rateService.getAuthUserData(userId);  // session userid가 authuserdata 테이블에 있는지 확인
    if (authUserData != null) {       // 1. 로그인 유저가 authUserData에 있으면
      // 1-1. 들어온 화면의 학원 코드와 인증한 학원 코드가 맞는지 확인
      if (code.equals(authUserData.getAcademyCode())) { // 해당 학원페이지 code와 유저 인증 학원code가 같다면
        //courseid/session 가져와서 과정명 선택 시 바로 띄워버리기
        int session = authUserData.getSession();
        String courseId = authUserData.getCourseId();
        Course courseForAcademy = rateService.getCourseByCourseidAndSession(courseId, session);
        model.addAttribute("course", courseForAcademy);
        model.addAttribute("academycode", code);
      } else {      // 1-2. 들어온 화면의 학원 코드와 인증한 학원 코드가 맞지 않으면
        // 인증된 학원 코드가 해당 페이지 학원과 맞지 않다고 튕겨내야함
      }
    } else { // 2. 로그인 유저가 authUserData에 없으면
      // 인증부터 하라고 튕겨내야함
      authUserData = null;
    }
    /* 학원 평점페이지 상단 근식님 정보 */
    Academy academy_info = academyService.getAcademyInfo(code);
    model.addAttribute("academy_info", academy_info);

    return "/view/academy_review-input";

  }

  /* 리뷰 작성 완료 후 확인 버튼 눌렀을 때 */
  @PostMapping("/review-input")
  public String reviewInput(
      @RequestParam("code") String code,
      @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
      @Valid RateDto rateDto, Model model) {

    rateDto.setRid(rateDto.getC_cid() + loginUser.getUserId());  // 코스 id + user id
    rateDto.setUserId(loginUser.getUserId());
    rateService.saveReview(rateDto);
    return "redirect:/";  // 해당 학원 평점 페이지로 다시 보내주면 좋은데
  }
}
