package com.gukbit;

import com.gukbit.domain.Rate;
import com.gukbit.repository.RateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class GukbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(GukbitApplication.class, args);
    }


//      @Bean
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
//
//    @Bean
//    public CommandLineRunner runner3(BoardRepository boardRepository) { //3. 명령 실행
//        return (args) -> {
//            IntStream.rangeClosed(1, 100).forEach(index ->
//                    boardRepository.save(Board.builder()
//                            .bid(index)
//                            .author("이순신")
//                            .date(LocalDateTime.now().minusDays(100 - index).format(
//                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
//                            .view((int) index)
//                            .title("국비학원 출신 10년차 개발자의 개인적인 의견입니다.")
//                            .content("")
//                            .b_academy_code("[멀티캠퍼스]")
//                            .b_course_id("[웹개발 풀스택 과정]")
//                            .visible(true)
//                            .recommend(0)
//                            .build())
//            );
//        };
//    }
    @Bean
    public CommandLineRunner runner4(RateRepository rateRepository) {
        return (args) -> {
            IntStream.rangeClosed(1, 100).forEach(index ->
                    rateRepository.save(Rate.builder()
                            .rid("AIG2021000031446811abc"+index+"def")
                            .cCid("AIG2021000031446811")
                            .userId("abc"+index+"def")
                            .oneStatement("배는 항구에 있을"+index+"때 가장 안전하다. 그러나 그것이 배의 존재 이유는 아니다.")
                            .lecturersEval((double) new Random().nextInt(5) + 1)
                            .curriculumEval((double) new Random().nextInt(5) + 1)
                            .employmentEval((double) new Random().nextInt(5) + 1)
                            .cultureEval((double) new Random().nextInt(5) + 1)
                            .facilityEval((double) new Random().nextInt(5) + 1)
                            .advantage("짧은 시"+index+"간내에 다양한 기술들을 배울 수 있었습니다.")
                            .disadvantage("너무 시간이 촉"+index+"박해서 개인 공부 시간이 더 필요합니다.")
                            .build())
            );
        };

    }
//

}



