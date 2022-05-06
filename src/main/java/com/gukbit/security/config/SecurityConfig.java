package com.gukbit.security.config;

import com.gukbit.security.config.oauth.CustomOauth2UserService;
import com.gukbit.security.config.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize라는 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomOauth2UserService customOauth2UserService;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider();
    }

    //정적인 data는 필터에 걸리지 않도록 처리...
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //로그인이 되어야 들어갈 수 있는 주소
                .antMatchers("/signUp").anonymous()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .loginPage("/loginForm")
                .usernameParameter("username")
                .loginProcessingUrl("/login")// /login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행합니다.
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
            .and()
                .oauth2Login()
                .loginPage("/loginForm") //구글 로그인이 완료된 후의 후처리가 필요 함. Tip. 코드 X (액세스 토큰 + 사용자 프로필 정보(O))
                .userInfoEndpoint()
                .userService(customOauth2UserService); //oauth의 loadUser에서 후처리
    }
}
