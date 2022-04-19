package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.domain.Course;
import com.gukbit.dto.AcademyDto;
import com.gukbit.dto.RateDto;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private RateRepository rateRepository;
  private CourseRepository courseRepository;

  public RateService(RateRepository rateRepository, CourseRepository courseRepository) {

    this.rateRepository = rateRepository;
    this.courseRepository = courseRepository;
  }

  @Transactional
  public void saveReview(RateDto rateDto) {
    System.out.println("save review 페이지 "+ rateDto);
    rateRepository.save(rateDto.toEntity());
  }

  public List<Course> getCoursesByAcademyCode(String academyCode) {
    System.out.println("rateService 도착" + academyCode);
    List<Course> courseListForAcademy = courseRepository.findByAcademycode(academyCode);
    System.out.println("RateService: courseListForAcademy"+courseListForAcademy);
    return courseListForAcademy;
  }
}
