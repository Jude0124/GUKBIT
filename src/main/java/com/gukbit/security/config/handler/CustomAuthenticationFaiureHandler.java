package com.gukbit.security.config.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFaiureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMesssage = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주십시오.";

        if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException){
            errorMesssage = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주십시오.";
        }else if(exception instanceof DisabledException){
            errorMesssage = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
        }else if(exception)
    }
}
