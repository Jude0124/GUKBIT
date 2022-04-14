package com.gukbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GukbitApplication {

  public static void main(String[] args) {
    SpringApplication.run(GukbitApplication.class, args);
  }

//  @Bean
//  public CommandLineRunner runner(AcademyRepository academyRepository) { //3. 명령 실행
//    Map<String, Data> map = AcademyList.academylist();
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

//  @Bean
//  public CommandLineRunner runner(BoardRepository boardRepository) { //3. 명령 실행
//    return (args) -> {
//      IntStream.rangeClosed(1, 100).forEach(index ->
//          boardRepository.save(Board.builder()
//              .bid(index)
//              .author("이순신")
//              .date(LocalDateTime.now())
//              .view(2222)
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
}
