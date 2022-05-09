package com.gukbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class GukbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(GukbitApplication.class, args);

    }
//    @Bean(name = "uploadPath")
//    public String uploadPath() {
//        return "src/main/resources/static/images/board";
//    }
}




