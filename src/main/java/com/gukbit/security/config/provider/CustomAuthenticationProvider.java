package com.gukbit.security.config.provider;

import com.gukbit.exception.UserLockException;
import com.gukbit.security.config.auth.CustomUserDetails;
import com.gukbit.security.config.auth.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws UsernameNotFoundException {
        System.out.println("CustomAuthenticationProvider.authenticate");
        String userId = authentication.getName();
        String password = (String)authentication.getCredentials();
        CustomUserDetails customUserDetails = (CustomUserDetails)customUserDetailsService.loadUserByUsername(userId);
        if(!bCryptPasswordEncoder.matches(password, customUserDetails.getPassword())){
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }else if(customUserDetails.getUser().getLockUser()){
            throw new UserLockException("해당 계정은 잠겼습니다.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
        System.out.println("authenticationToken = " + authenticationToken);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
