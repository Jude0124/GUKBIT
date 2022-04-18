package com.gukbit.service;

import com.gukbit.dto.domain.User;
import com.gukbit.etc.LoginData;
import com.gukbit.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    //null 이면 로그인 실패
    public User login(LoginData loginData, BindingResult bindingResult) {
        User user = userRepository.findByUserId(loginData.getId());
        if(user == null){
            bindingResult.addError(new FieldError("loginData", "id", "일치하는 아이디가 없습니다."));
            return null;
        }else{
            System.out.println("loginUser.getPw() = " + user.getPassword());
            if(loginData.getPw().equals(user.getPassword())){
                return user;
            }else{
                bindingResult.addError(new FieldError("loginData", "pw", "비밀번호가 일치하지 않습니다."));
                return null;
            }
        }
    }


}
