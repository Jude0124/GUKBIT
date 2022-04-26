package com.gukbit.controller;

import com.gukbit.domain.Course;
import com.gukbit.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course/courseData")
    public String getCourseDataMapping(@RequestParam(value = "CourseId") String courseId, Model model){
        List<Course> list = courseService.getCourseData(courseId);
        model.addAttribute("courseList", list);
        return  "fragments/course-data-option";
    }
}
