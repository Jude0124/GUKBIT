package com.gukbit.service;

import com.gukbit.domain.User;
import com.gukbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    //null 이면 로그인 실패
    public User login(String id, String pw) {
        return userRepository.findById(id).filter(user -> user.getPw().equals(pw)).orElse(null);
    }


}
