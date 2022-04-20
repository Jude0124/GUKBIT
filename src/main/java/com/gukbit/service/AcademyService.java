package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.domain.Board;
import com.gukbit.domain.Course;
import com.gukbit.dto.AcademyDto;
import com.gukbit.repository.AcademyRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import com.gukbit.repository.CourseRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AcademyService {

  private AcademyRepository academyRepository;
  private CourseRepository courseRepository;

  public AcademyService(AcademyRepository academyRepository,CourseRepository courseRepository) {
    this.academyRepository = academyRepository;
    this.courseRepository = courseRepository;
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
        .build();
  }

  public Academy getAcademyInfo(String code){
    return academyRepository.findByCode(code);
  }

  public Page<Course> expectedCoursePageList(String code, Pageable pageable){
    Sort sort = Sort.by("start").descending();
    List<Course> list = courseRepository.findAllByAcademyCode(code);

    pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5,sort);

    final int start = (int)pageable.getOffset();
    final int end = Math.min((start + pageable.getPageSize()), list.size());
    final Page<Course> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
    return page;
  }
}
