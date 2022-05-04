package com.gukbit.service;

import com.gukbit.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserService userService;
    private final BoardService boardService;

    public List<User> getUserList(){
        return userService.getUserList();
    }

    public void deleteUser(String userId){
        userService.deleteUser(userService.getUserByUserId(userId));
    }
}
