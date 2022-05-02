package com.gukbit.config.auth;

import com.gukbit.domain.User;
import com.gukbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//시큐리티 설정에서 loginProsessingUrl("/login");
///login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername함수가 실행
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUserId(userId);
        System.out.println("userEntity = " + userEntity);
        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
