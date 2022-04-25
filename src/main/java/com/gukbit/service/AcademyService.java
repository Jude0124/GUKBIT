package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.domain.Course;
import com.gukbit.domain.Rate;
import com.gukbit.dto.AcademyDto;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AcademyService {

  private AcademyRepository academyRepository;
  private CourseRepository courseRepository;
  private RateRepository rateRepository;

  public AcademyService(AcademyRepository academyRepository,CourseRepository courseRepository, RateRepository rateRepository) {
    this.academyRepository = academyRepository;
    this.courseRepository = courseRepository;
    this.rateRepository = rateRepository;
  }

  @Transactional
  public List<AcademyDto> searchAcademy(String keyword) {
    List<Academy> academies = academyRepository.findByNameContaining(keyword);
    List<AcademyDto> academyDtoList = new ArrayList<>();

    if(academies.isEmpty()) return academyDtoList;
    for(Academy academy : academies){
      academyDtoList.add(this.convertEntityToDto(academy));
    }
    return academyDtoList;
  }

  private AcademyDto convertEntityToDto(Academy academy){
    return AcademyDto.builder()
        .code(academy.getCode())
        .name(academy.getName())
        .home_url(academy.getHome_url())
        .region(academy.getRegion())
        .addr(academy.getAddr())
        .eval(academy.getEval())
        .tel(academy.getTel())
        .imageUrl(academy.getImageUrl())
        .build();
  }

  public Academy getAcademyInfo(String code){
    return academyRepository.findByCode(code);
  }

  public double[] reviewCourseAverage(List<Course> courses){
      double[] list = new double[6];
      List<String> listId = new ArrayList<>();
      List<Rate> listAll = new ArrayList<>();
      for(Course course: courses){
          listId.add(course.getCid());
      }
     listAll.addAll(rateRepository.findAllBycCidIn(listId));
      for(int i =0 ; i< listAll.size() ; i++){
          list[0] += listAll.get(i).getLecturersEval();
          list[1] += listAll.get(i).getCurriculumEval();
          list[2] += listAll.get(i).getEmploymentEval();
          list[3] += listAll.get(i).getCultureEval();
          list[4] += listAll.get(i).getFacilityEval();
      }
      list[5] = (list[0]+list[1]+list[2]+list[3]+list[4])/5;
      for (int i = 0 ; i < list.length ; i++){
          list[i] /= listAll.size();

          list[i] = Math.round(list[i]*10.0)/10.0;
      }
      return list;
  }



  public Page<Rate> reviewCoursePageList(List<Course> courses, Pageable pageable) {
      List<Rate> list = new ArrayList<>();
      List<String> listId = new ArrayList<>();
      for(Course course : courses){
          listId.add(course.getCid());
  }
      list.addAll(rateRepository.findAllBycCidIn(listId));


      pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5);

      final int start = (int)pageable.getOffset();
      final int end = Math.min((start + pageable.getPageSize()), list.size());
      final Page<Rate> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
      System.out.println("학원 리뷰 :"  +page);

      return page;
  }


  public Page<Course> expectedCoursePageList(String code, Pageable pageable){
    List<Course> list = courseRepository.findAllByAcademyCode(code);

    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String formatedNow = now.format(formatter);

    List<Course> expectedList = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {
      int startDate = Integer.parseInt(list.get(i).getStart().replaceAll("-",""));
      if((startDate - Integer.parseInt(formatedNow)) > 0){
        expectedList.add(list.get(i));
      }
    }

    pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5);

    final int start = (int)pageable.getOffset();
    final int end = Math.min((start + pageable.getPageSize()), expectedList.size());
    final Page<Course> page = new PageImpl<>(expectedList.subList(start, end), pageable, expectedList.size());
    return page;
  }
}
