package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.domain.Course;
import com.gukbit.domain.Division_S;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.Division_sRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class indexService {

    @Autowired
    Division_sRepository division_sRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    AcademyRepository academyRepository;
    public List<Division_S> selectSlideMenu () {
        return division_sRepository.findAll();
    }

    public List<Course> getfield_sCourses (String field_s) {
        return courseRepository.findAllByFields(field_s);
    }

    public Academy getOneCodeAcademy (String code) {
        return academyRepository.findByCode(code);
    }

}
