package com.gukbit.security.config.handler;

import com.gukbit.exception.UserLockException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMesssage = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주십시오.";
        if(exception instanceof UsernameNotFoundException){
            errorMesssage = "존재하지 않는 아이디입니다. 다시 확인해 주십시오.";
        }else if(exception instanceof BadCredentialsException){
            errorMesssage = "비밀번호가 맞지 않습니다. 다시 확인해 주십시오.";
        }else if(exception instanceof DisabledException){
            errorMesssage = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
        }else if(exception instanceof CredentialsExpiredException){
            errorMesssage = "비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요";
        }else if(exception instanceof UserLockException){
            errorMesssage = "계정이 잠겼습니다. 관리자에게 문의하세요";
        }
        setDefaultFailureUrl("/loginForm?error=true&exception=" + URLEncoder.encode(errorMesssage,"UTF-8"));
        super.onAuthenticationFailure(request,response,exception);
    }
}
