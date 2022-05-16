package com.gukbit.service;

import com.gukbit.domain.Course;
import com.gukbit.domain.DivisionS;
import com.gukbit.domain.Rate;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.DivisionSRepository;
import com.gukbit.repository.RateRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {

    @Autowired
    DivisionSRepository DivisionSRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    AcademyRepository academyRepository;

    @Autowired
    RateRepository rateRepository;
    
    // INDEX: NEW리뷰 정렬 후 반환
    public List<Rate> selectSideReviewList(){

        List<Rate> rates = rateRepository.findAllByRateSort();
        List<Rate> ratesTemp= new ArrayList<>();
        for(int i=0; i<10; i++) {
            ratesTemp.add(isImage(rates.get(i)));

        }
        ratesTemp.add(rates.get(0));
        return ratesTemp;
    }

    // INDEX: 학원 슬라이드 메뉴 반환
    public List<DivisionS> selectSlideMenu() {
        // DivisionSRepository.findAll();
        List<DivisionS> divS = new ArrayList<>();
        divS.add(new DivisionS("2001", "정보기술"));
        divS.add(new DivisionS("200102", "정보개발"));
        divS.add(new DivisionS("200106", "정보보호"));
        divS.add(new DivisionS("200107", "인공지능"));
        divS.add(new DivisionS("200108", "블록체인"));
        divS.add(new DivisionS("200109", "스마트물류"));
        divS.add(new DivisionS("200110", "디지털트윈"));
        divS.add(new DivisionS("2002", "통신기술"));
        divS.add(new DivisionS("2003", "방송기술"));

        return divS;
    }

    public List<Course> getCodeAcademy(String tag, String local) {
        List<Course> courses;
        if (tag.equals("2001")) {
            String[] divS = { "200101", "200103", "200104", "200105" };
            courses = courseRepository.findAllByFieldSIn(divS);
        } else {
            courses = courseRepository.findAllByFieldSStartingWith(tag);
        }
        List<Course> coursesTemp = new ArrayList<>();
        List<Course> distCoursesTemp = new ArrayList<>();

        int localNum = Integer.parseInt(local);
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

        /* 이미지 Link http://localhost:9090/academy/review?code=undefined */

        for (int imgCount = 0; imgCount < courses.size(); imgCount++) {
            coursesTemp.add(isImage(courses.get(imgCount)));
        }


        /* NCS와 코드에 따라 List 재입력 */
        if (localNum == 10) { // 전체출력
            distCoursesTemp = coursesTemp;
        } else { // 지역선택시
            for (int j = 0; j < coursesTemp.size(); j++) {
                for (int i = 0; i < localData.size(); i++) {
                    if (coursesTemp.get(j).getAcademy().getRegion().contains(localData.get(i))) {
                        distCoursesTemp.add(coursesTemp.get(j));
                    }
                }
            }
        }

        return distCoursesTemp;
    }


    /* Course, Rate 도메인별 이미지 불러오는 용도 */
    public Course isImage(Course course){
        String[] fne = { ".jpg", ".png", ".gif", ".bmp" };

        for (String fnet : fne) {
            String url = "static/images/academy/";
            String fileName = course.getAcademyCode() + fnet;
            url += fileName;
            try {
//                 File file = new ClassPathResource(url).getFile();
                InputStream inputStream = new ClassPathResource(url).getInputStream();
                File file = File.createTempFile("temp",fnet);
                FileUtils.copyInputStreamToFile(inputStream, file);
                if (file.isFile()) {
                    course.getAcademy().setImageUrl(fileName);
                    break;
                }
            } catch (IOException e) {
                course.getAcademy().setImageUrl("NoAcademyImage.png");
            }
        }

        return course;
    }



    public Rate isImage(Rate rate){
        String[] fne = { ".jpg", ".png", ".gif", ".bmp" };

        for (String fnet : fne) {
            String url = "static/images/academy/";
            String fileName = rate.getCourse().getAcademyCode() + fnet;
            url += fileName;
            try {
                // File file = new ClassPathResource(url).getFile();
                InputStream inputStream = new ClassPathResource(url).getInputStream();
                File file = File.createTempFile("temp",fnet);
                FileUtils.copyInputStreamToFile(inputStream, file);
                if (file.isFile()) {
                    rate.getCourse().getAcademy().setImageUrl(fileName);
                    break;
                }
            } catch (IOException e) {
                rate.getCourse().getAcademy().setImageUrl("NoAcademyImage.png");
            }
        }

        return rate;
    }



}
