package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.domain.Course;
import com.gukbit.domain.Rate;
import com.gukbit.dto.AcademyDto;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.CourseRepository;
import com.gukbit.repository.RateRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class AcademyService {

  private AcademyRepository academyRepository;
  private CourseRepository courseRepository;
  private RateRepository rateRepository;
  private IWordAnalysisService wordAnalysisService;

  public AcademyService(AcademyRepository academyRepository,CourseRepository courseRepository, RateRepository rateRepository ,IWordAnalysisService wordAnalysisService) {
    this.academyRepository = academyRepository;
    this.courseRepository = courseRepository;
    this.wordAnalysisService = wordAnalysisService;
    this.rateRepository = rateRepository;
  }

  public List<Academy> searchAllAcademy(){
    List<Academy> academyList = academyRepository.findAll();
    return academyList;
  }

  @Transactional
  public List<AcademyDto> searchAcademy(String keyword) {
    List<Academy> academies = academyRepository.findByNameContaining(keyword);
    List<AcademyDto> academyDtoList = new ArrayList<>();
    List<Academy> academiesTemp = new ArrayList<>();

    for(int imgCount=0; imgCount<academies.size(); imgCount++){
      academiesTemp.add(isImage(academies.get(imgCount)));
    }

    if(academiesTemp.isEmpty()) return academyDtoList;
    for(Academy academy : academiesTemp){
      academyDtoList.add(this.convertEntityToDto(academy));
    }
    return academyDtoList;
  }

  private AcademyDto convertEntityToDto(Academy academy){
    return AcademyDto.builder()
        .code(academy.getCode())
        .name(academy.getName())
        .homeUrl(academy.getHomeUrl())
        .region(academy.getRegion())
        .addr(academy.getAddr())
        .eval(academy.getEval())
        .tel(academy.getTel())
        .imageUrl(academy.getImageUrl())
        .build();
  }

  public Academy getAcademyInfo(String code){
    Academy academyInfo = academyRepository.findByCode(code);
    academyInfo = isImage(academyInfo);
    return academyInfo;


  }

  public double[] reviewCourseAverage(List<Course> courses){
      double[] list = new double[7];
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
      for (int i = 0 ; i < 6 ; i++){
          list[i] /= listAll.size();

          list[i] = Math.round(list[i]*10.0)/10.0;
      }
      list[6] = listAll.size();
      return list;
  }


  @Transactional
  public Page<Rate> reviewCoursePageList(List<Course> courses, Pageable pageable) {
      List<Rate> list = new ArrayList<>();
      List<String> listId = new ArrayList<>();
      for(Course course : courses){
          listId.add(course.getCid());
      }

      list.addAll(rateRepository.findAllBycCidIn(listId));


      Collections.sort(list, (s1,s2) -> {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          Date date1 = null;
          Date date2 = null;
          try {
              /*String -> Date*/
              date1 = formatter.parse(s1.getDate());
              date2 = formatter.parse(s2.getDate());
          } catch (ParseException e) {
              e.printStackTrace();
          }

          /* Date 객체 끼리 compareTo*/
          return date2.compareTo(date1);
      });

      for (Rate rate : list) {
          System.out.println("rate.getDate() = " + rate.getDate());
      }
      
      pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, 5);

      final int start = (int)pageable.getOffset();
      final int end = Math.min((start + pageable.getPageSize()), list.size());
      final Page<Rate> page = new PageImpl<>(list.subList(start, end), pageable, list.size());

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



  /* 이미지 입력 및 이미지 확인 여부 */
  public Academy isImage(Academy academy){

    String[] fne = {".jpg", ".png", ".gif", ".bmp"};

    for(String fnet : fne) {
      String url = "static/images/academy/";
      String fileName = academy.getCode() + fnet;
      url += fileName;
      try {
        // File file = new ClassPathResource(url).getFile();
          InputStream inputStream = new ClassPathResource(url).getInputStream();
          File file = File.createTempFile("temp",fnet);
          FileUtils.copyInputStreamToFile(inputStream, file);
        if (file.isFile()) {
          academy.setImageUrl(fileName);
          break;
        }
      }catch (IOException e){
        academy.setImageUrl("NoAcademyImage.png");
      }
    }

    return academy;
  }



  public List<Map<String,Integer>> analysis(String academyCode) throws Exception {


    //분석할 문장
    //String text = "아침에 밥을 꼭 먹고 점심엔 점심 밥을 꼭 먹고 저녁엔 저녁 밥을 꼭 먹자!";

    //신조어 및 새롭게 생겨난 가수 및 그룹명은 제대로 된 분석이 불가능합니다.
    // 새로운 명사 단어들은 어떻게 데이터를 처리해야 할까?? => 데이터사전의 주기적인 업데이트


    List<Map<String,Integer>> list = new ArrayList<>(wordAnalysisService.doWordAnalysis(academyCode));


    return list;
  }
}
