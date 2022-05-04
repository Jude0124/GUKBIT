package com.gukbit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmailMessage(String email) throws Exception {
        String code = createCode(); // 인증코드 생성
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
        message.setSubject("[GUKBIT] 인증 코드 입니다."); // 이메일 제목
        message.setText(setContext(code), "utf-8", "html"); // 내용 설정

        emailSender.send(message); // 이메일 전송
    }

    private String setContext(String code) { // 보낼 내용
        Context context = new Context();
        context.setVariable("code", code); // Template에 코드 전달
        return templateEngine.process("view/user/mail", context); // mail.html
    }

    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 7; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0: // a ~ z
                    code.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1: // A ~ Z
                    code.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2: // 0 ~ 9
                    code.append((rnd.nextInt(10)));
                    break;
            }
        }
        return code.toString();
    }
}
