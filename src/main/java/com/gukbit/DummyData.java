package com.gukbit;

import com.gukbit.domain.Rate;
import com.gukbit.repository.RateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class DummyData {
    public static void main(String[] args) {
        SpringApplication.run(DummyData.class,args);
    }
    
//    @Bean
//    public CommandLineRunner runner(AcademyRepository academyRepository) { //3. 명령 실행
//        Map<String, AcademyData> map = AcademyList.academylist();
//        return (args) -> {
//            map.forEach((key, value) ->
//                academyRepository.save(Academy.builder()
//                    .code(value.getAcademyId())
//                    .name(value.getAcademyName())
//                    .home_url(value.getHpAddr())
//                    .region(value.getRegion())
//                    .addr(value.getAddr())
//                    .tel(value.getTel())
//                    .build())
//            );
//        };
//    }
//
//    @Bean
//    public CommandLineRunner runner2(CourseRepository courseRepository) { //3. 명령 실행
//        Map<Integer, CourseData> map = CourseList.courselist();
//        return (args) -> {
//            map.forEach((key, value) ->
//                courseRepository.save(Course.builder()
//                    .academy_code(value.getAcademyId())
//                    .id(value.getCourseId())
//                    .session(Integer.parseInt(value.getSession()))
//                    .field_m(value.getField_m())
//                    .field_s(value.getField_s())
//                    .dFieldSs(value.getDFieldSs())
//                    .name(value.getTitle())
//                    .start(value.getStart())
//                    .end(value.getEnd())
//                    .build())
//            );
//        };
//    }

//
//    @Bean
//    public CommandLineRunner runner(AcademyRepository academyRepository) { //3. 명령 실행
//        Map<String, AcademyData> map = AcademyList.academylist();
//        return (args) -> {
//            map.forEach((key, value) ->
//                academyRepository.save(Academy.builder()
//                    .code(value.getAcademyId())
//                    .name(value.getAcademyName())
//                    .home_url(value.getHpAddr())
//                    .region(value.getRegion())
//                    .addr(value.getAddr())
//                    .tel(value.getTel())
//                    .build())
//            );
//        };
//    }
//    @Bean
//    public CommandLineRunner runner3(BoardRepository boardRepository) { //3. 명령 실행
//        return (args) -> {
//            IntStream.rangeClosed(1, 100).forEach(index ->
//                boardRepository.save(Board.builder()
//                    .bid(index)
//                    .author("이순신")
//                    .date(LocalDateTime.now().minusDays(100 - index).format(
//                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
//                    .view((int) index)
//                    .title("국비학원 출신 10년차 개발자의 개인적인 의견입니다.")
//                    .content("")
//                    .bAcademyName("[멀티캠퍼스]")
//                    .b_academy_code("500020039927")
//                    .b_course_name("[웹개발 풀스택 과정]")
//                    .b_course_id("[AIG20210000328860]")
//                    .visible(true)
//                    .recommend(0)
//                    .build())
//            );
//        };
//    }
//
//    @Bean
//    public CommandLineRunner runner4(RateRepository rateRepository) {
//        return (args) -> {
//            IntStream.rangeClosed(1, 100).forEach(index ->
//                rateRepository.save(Rate.builder()
//                    .rid("AIG2021000031446811abc" + index + "def")
//                    .cCid("AIG2021000031446811")
//                    .userId("abc" + index + "def")
//                    .oneStatement("배는 항구에 있을" + index + "때 가장 안전하다. 그러나 그것이 배의 존재 이유는 아니다.")
//                    .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                    .lecturersEval((double) new Random().nextInt(5) + 1)
//                    .curriculumEval((double) new Random().nextInt(5) + 1)
//                    .employmentEval((double) new Random().nextInt(5) + 1)
//                    .cultureEval((double) new Random().nextInt(5) + 1)
//                    .facilityEval((double) new Random().nextInt(5) + 1)
//                    .advantage("이상 이상의 보이는 그러므로 있음으로써 따뜻한 뼈 곳이 힘있다. 들어 과실이 창공에 있을 긴지라 속잎나고, 끓는다. 유소년에게서 얼마나 이상은 예수는 위하여 사막이다. 트고, 맺어, 때에, 같이 구하지 이것이다. 튼튼하며, 찬미를 동산에는 부패를 천하를 소금이라 끓는 봄바람이다. 이상 있는 돋고, 풍부하게 뜨고, 어디 봄바람이다. 실로 희망의 얼마나 생생하며, 이것은 열락의 인도하겠다는 인간은 황금시대다. 생의 있는 너의 하는 황금시대다. 하는 황금시대의 피가 우리는 보이는 구하지 때까지 끝까지 품었기 뿐이다. 두손을 별과 거친 사람은 들어 그것을 무엇을 때문이다.\n")
//                    .disadvantage("시와 벌레는 동경과 버리었습니다. 한 아직 내 ㄸ에 봅니다. 하나 추억과 별 다하지 그리워 나는 이름을 있습니다. 이제 당신은 이름자 릴케 별 잔디가 없이 까닭이요, 다 까닭입니다. 책상을 이름을 나는 오면 불러 아름다운 토끼, 나의 버리었습니다. 별 하나에 별을 듯합니다. 어머니, 마리아 이름과, 거외다. 청춘이 사랑과 겨울이 나는 불러 가득 이름자 소녀들의 거외다. 이 부끄러운 내린 버리었습니다. 청춘이 이국 까닭이요, 내일 이름과, 사랑과 계십니다.")
//                    .build())
//            );
//        };
//    }

}
