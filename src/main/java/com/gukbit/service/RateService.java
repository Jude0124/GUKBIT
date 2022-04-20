package com.gukbit.service;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.Course;
import com.gukbit.dto.RateDto;
import com.gukbit.repository.AuthUserDataRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
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
    System.out.println("save review 페이지 " + rateDto);
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
}
