package com.gukbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CourseController {
    @GetMapping("/course/courseData")
    @ResponseBody
    public String getCourseDataMapping(@RequestParam(value = "CourseName") String courseName){
        System.out.println("courseName = " + courseName);
        return null;
    }
}
