package com.gukbit.service;

import com.gukbit.domain.Course;
import com.gukbit.domain.Division_S;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.Division_sRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public List <Course> getCodeAcademy (String tag, String local) {
            List<Course> courses = courseRepository.findAllByFields(tag);
            List<Course> Dist_coursesTemp = new ArrayList<>();

            int localNum = Integer.parseInt(local);
            System.out.println(localNum);
            // 지역을 저장하기 위한 LIST
            List<String> localData = new ArrayList<>();


            if (localNum == 10) {

            } else if (localNum == 11) {
                localData.add("서울");
            } else if (localNum == 41) {
                localData.add("경기");
            } else if (localNum == 28) {
                localData.add("인천");
            } // 42 이상
            else if (localNum == 43) {
                localData.add("충북");
                localData.add("충남");
                localData.add("세종");
                localData.add("대전");
            } else if (localNum == 45) {
                localData.add("전북");
                localData.add("전남");
                localData.add("광주 ");
            } else if (localNum == 47) {
                localData.add("경북");
                localData.add("경남");
                localData.add("부산");
                localData.add("대구");
            } else if (localNum == 51) {
                localData.add("제주");
                localData.add("강원");
            }

            for(int imgCount=0; imgCount<courses.size(); imgCount++){
                String[] fne = {".jpg", ".png", ".gif", ".bmp"};

                for(String fnet : fne) {
                    String url = "static/images/academy/";
                    String fileName = courses.get(imgCount).getAcademycode() + fnet;
                    url += fileName;
                    try {
                        File file = new ClassPathResource(url).getFile();
                        if (file.isFile()) {
                            courses.get(imgCount).getAcademy().setImageUrl(fileName);
                        }
                    }catch (IOException e){
                    }
                }
            }

            if (localNum == 10) { //전체출력
                Dist_coursesTemp = courses;
            } else { // 지역선택시
                for(int j=0; j<courses.size(); j++) {
                    for (int i = 0; i < localData.size(); i++) {
                        if (courses.get(j).getAcademy().getRegion().contains(localData.get(i)))
                        {
                            Dist_coursesTemp.add(courses.get(j));
                        }
                    }
                }
            }

        return Dist_coursesTemp;
    }

}
