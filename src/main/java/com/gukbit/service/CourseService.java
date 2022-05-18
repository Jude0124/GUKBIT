package com.gukbit.service;

import com.gukbit.domain.Course;
import com.gukbit.domain.DivisionS;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.DivisionSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private DivisionSRepository divisionSRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, DivisionSRepository divisionSRepository) {
        this.courseRepository = courseRepository;
        this.divisionSRepository = divisionSRepository;
    }

    //코스 아이디를 통해 존재하는 코드를 리스트로 반환
    public List<Course> getCourseData(String courseId){
        return courseRepository.findAllById(courseId);
    }
    public Course getCourseByIdAndSession(String courseId, int session) {
        Course courseForAcademy = courseRepository.findByIdAndSession(courseId, session);
        return courseForAcademy;
    }

    public List<DivisionS> getAllDivisionS(){
        return divisionSRepository.findAll();
    }

    // 학원 아이디를 통해 학원아이디와 일치하는 과정을 리스트로 반환
    public List<Course> getCourseList(String code) { return courseRepository.findAllByAcademyCode(code); }


    public List<Course> getCourseListByAcademyId(String academyId){
        return courseRepository.findAllByAcademyCode(academyId);
    }

}
