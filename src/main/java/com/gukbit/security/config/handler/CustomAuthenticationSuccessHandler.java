package com.gukbit.security.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.gukbit.controller.LoginController.prevPage;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String[] list = prevPage.split("/|\\?");
        List<String> urlList = List.of(list);
        System.out.println("urlList = " + urlList);
        if (!list[list.length - 1].equals("signUp") && !list[list.length - 1].equals("mypage")
                && !list[list.length - 1].equals("processRegister") && !urlList.contains("admin")
                && !urlList.contains("loginForm")) {
            setDefaultTargetUrl(prevPage);
        } else {
            setDefaultTargetUrl("/");
        }
        redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
    }
}
