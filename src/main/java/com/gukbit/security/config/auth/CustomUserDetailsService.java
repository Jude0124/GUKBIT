package com.gukbit.security.config.auth;

import com.gukbit.domain.User;
import com.gukbit.exception.UserLockException;
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
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException, UserLockException {
        User user = userRepository.findByUserId(userId);
        if(user == null){
            throw new UsernameNotFoundException("해당 아이디의 유저가 없습니다.");
        }

        if(user.getLock()){
            throw new UserLockException("해당 계정은 잠겼습니다.");
        }

        return new CustomUserDetails(user);
    }
}
