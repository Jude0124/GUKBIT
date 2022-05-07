package com.gukbit;

import com.gukbit.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailService emailService;

    @Test
    void sendMail() throws Exception {
        emailService.sendEmailMessage("narhwal7@naver.com");
    }
}
