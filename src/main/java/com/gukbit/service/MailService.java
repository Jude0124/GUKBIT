package com.gukbit.service;

import com.gukbit.domain.User;
import com.gukbit.etc.RedisUtil;
import com.gukbit.repository.UserRepository;
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
public class MailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;

    public String sendEmailMessage(String email){
        String code = createCode(); // 인증코드 생성
        try {
            redisUtil.setDataExpire(code, email, 60*5L); // 유효기간 5분

            MimeMessage message = emailSender.createMimeMessage();
            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[GUKBIT] 인증 코드 입니다."); // 이메일 제목
            message.setText(setContext(code), "utf-8", "html"); // 내용 설정
            emailSender.send(message); // 이메일 전송

        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    private String setContext(String code) { // 보낼 내용
        Context context = new Context();
        context.setVariable("code", code); // Template에 코드 전달
        return templateEngine.process("view/user/user-mail", context); // user-mail.html
    }

    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
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


    public String getUserIdByCode(String code) {
        String email = redisUtil.getData(code); // 입력 받은 인증 코드(key)를 이용해 email(value)을 꺼낸다.
        if (email == null) { // email이 존재하지 않으면, 유효 기간 만료이거나 코드 잘못 입력
            System.out.println("유효 기간 만료 or 코드 잘못 입력");
            return "fail";
        }

        User user = userRepository.findByEmail(email); // 해당 email로 user를 꺼낸다.
        return user.getUserId();
    }
}
