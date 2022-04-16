package com.gukbit.controller;

import com.gukbit.domain.User;
import com.gukbit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/process_register")
  public String processRegistration(User user) {
    userService.joinUser(user);
    return "/view/register_success";
  }

}