package com.gukbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
=======
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.LongStream;
>>>>>>> ee019b14c266127f624f28180d3775b07a2c6437

@SpringBootApplication
public class GukbitApplication {

  public static void main(String[] args) {
    SpringApplication.run(GukbitApplication.class, args);
  }

<<<<<<< HEAD

=======
>>>>>>> ee019b14c266127f624f28180d3775b07a2c6437
//  @Bean
//  public CommandLineRunner runner(AcademyRepository academyRepository) { //3. 명령 실행
//    Map<String, AcademyData> map = AcademyList.academylist();
//    return (args) -> {
//      map.forEach((key, value) ->
//              academyRepository.save(Academy.builder()
//                      .code(value.getAcademyId())
//                      .name(value.getAcademyName())
//                      .home_url(value.getHpAddr())
//                      .region(value.getRegion())
//                      .addr(value.getAddr())
//                      .tel(value.getTel())
//                      .build())
//      );
//    };
//  }
//
<<<<<<< HEAD
//    @Bean
//    public CommandLineRunner runner2(CourseRepository courseRepository) { //3. 명령 실행
//      Map<Integer, CourseData> map = CourseList.courselist();
//      return (args) -> {
//        map.forEach((key, value) ->
//                courseRepository.save(Course.builder()
//                        .academy_code(value.getAcademyId())
//                        .id(value.getCourseId())
//                        .session(Integer.parseInt(value.getSession()))
//                        .field_m(value.getField_m())
//                        .field_s(value.getField_s())
//                        .d_field_ss(value.getD_field_ss())
//                        .name(value.getTitle())
//                        .start(value.getStart())
//                        .end(value.getEnd())
//                        .build())
//        );
//      };
//    }
=======
//  @Bean
//  public CommandLineRunner runner2(CourseRepository courseRepository) { //3. 명령 실행
//    Map<Integer, CourseData> map = CourseList.courselist();
//    return (args) -> {
//      map.forEach((key, value) ->
//              courseRepository.save(Course.builder()
//                      .academy_code(value.getAcademyId())
//                      .id(value.getCourseId())
//                      .session(Integer.parseInt(value.getSession()))
//                      .field_m(value.getField_m())
//                      .field_s(value.getField_s())
//                      .d_field_ss(value.getD_field_ss())
//                      .name(value.getTitle())
//                      .start(value.getStart())
//                      .end(value.getEnd())
//                      .build())
//      );
//    };
//  }
>>>>>>> ee019b14c266127f624f28180d3775b07a2c6437
//
//  @Bean
//  public CommandLineRunner runner3(BoardRepository boardRepository) { //3. 명령 실행
//    return (args) -> {
//      LongStream.rangeClosed(1, 100).forEach(index ->
//          boardRepository.save(Board.builder()
<<<<<<< HEAD
//              .bid((long) index)
//              .author("이순신")
//              .date(String.valueOf(LocalDateTime.now()))
//              .view(2222)
=======
//              .bid(index)
//              .author("이순신")
//              .date(LocalDateTime.now().toString())
//              .view((int)index)
>>>>>>> ee019b14c266127f624f28180d3775b07a2c6437
//              .title("국비학원 출신 10년차 개발자의 개인적인 의견입니다.")
//              .content("")
//              .b_academy_code("[멀티캠퍼스]")
//              .b_course_id("[웹개발 풀스택 과정]")
//              .visible(true)
//              .recommend(0)
//              .build())
//      );
//    };
//  }
<<<<<<< HEAD

=======
>>>>>>> ee019b14c266127f624f28180d3775b07a2c6437
}
