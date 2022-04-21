package com.gukbit.service;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.domain.Rate;
import com.gukbit.dto.RateDto;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private RateRepository rateRepository;
  private CourseRepository courseRepository;
  private AuthUserDataRepository authUserDataRepository;

  public RateService(RateRepository rateRepository, CourseRepository courseRepository,
      AuthUserDataRepository authUserDataRepository) {

    this.rateRepository = rateRepository;
    this.courseRepository = courseRepository;
    this.authUserDataRepository = authUserDataRepository;
  }

  @Transactional
  public void saveReview(RateDto rateDto) {
    rateRepository.save(rateDto.toEntity());
  }

  public List<Course> getCoursesByAcademyCode(String academyCode) {
//    System.out.println("rateService 도착" + academyCode);
    List<Course> courseListForAcademy = courseRepository.findByAcademycode(academyCode);
//    System.out.println("RateService: courseListForAcademy"+courseListForAcademy);
    return courseListForAcademy;
  }

  public Course getCourseByCourseidAndSession(String courseId, int session) {
    Course courseForAcademy = courseRepository.findByIdAndSession(courseId, session);
    return courseForAcademy;
  }

  public AuthUserData getAuthUserData(String userId) {
    AuthUserData authUserData = authUserDataRepository.findByUserId(userId);
    return authUserData;
  }

  public RateDto findByRid(String rid) {
    Rate rateByRid = rateRepository.findByRid(rid);
    RateDto rateDtoByRid;

    rateDtoByRid = RateDto.builder()
        .rid(rateByRid.getRid())
        .c_cid(rateByRid.getC_cid())
        .userId(rateByRid.getUserId())
        .lecturers_eval(rateByRid.getLecturers_eval())
        .curriculum_eval(rateByRid.getCurriculum_eval())
        .employment_eval(rateByRid.getEmployment_eval())
        .culture_eval(rateByRid.getCulture_eval())
        .facility_eval(rateByRid.getFacility_eval())
        .advantage(rateByRid.getAdvantage())
        .disadvantage(rateByRid.getDisadvantage())
        .one_statement(rateByRid.getOne_statement())
        .build();
    return rateDtoByRid;
  }
}
