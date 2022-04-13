package com.gukbit;

import com.gukbit.domain.Board;
import com.gukbit.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class GukbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(GukbitApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(BoardRepository boardRepository) { //3. 명령 실행
        return (args) -> {
            IntStream.rangeClosed(1, 300).forEach(index ->
                    boardRepository.save(Board.builder()
                            .no(index)
                            .author("이순신")
                            .date(LocalDateTime.now())
                            .view(2222)
                            .title("국비학원 출신 10년차 개발자의 개인적인 의견입니다.")
                            .content("")
                            .academyCode("[멀티캠퍼스]")
                            .courseId("[웹개발 풀스택 과정]")
                            .visible(true)
                            .recommend(0)
                            .build())
            );
        };
    }
}
