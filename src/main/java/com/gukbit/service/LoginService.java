package com.gukbit.service;

import com.gukbit.domain.User;
import com.gukbit.etc.LoginData;
import com.gukbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    //null 이면 로그인 실패
    public User login(LoginData loginData, Map<String,String> errors) {
        Optional<User> user = userRepository.findById(loginData.getId());
        if(user.isEmpty()){
            errors.put("noId", "일치하는 아이디가 없습니다.");
            return null;
        }else{
            User loginUser = user.get();
            System.out.println("loginUser.getPw() = " + loginUser.getPw());
            if(loginData.getPw().equals(loginUser.getPw())){
                return loginUser;
            }else{
                errors.put("noEqualsPw", "비밀번호가 일치하지 않습니다.");
                return null;
            }
        }
    }


}
