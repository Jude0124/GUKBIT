package com.gukbit.controller;

import com.gukbit.domain.User;
import com.gukbit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

//  회원가입
  @PostMapping("/process_register")
  public String processRegistration(User user) {
    userService.joinUser(user);
    return "/view/register_success";
  }

//  아이디 중복확인
  @PostMapping("/idCheck")
  @ResponseBody
  public int IdCheck(@RequestBody String id) throws Exception {

    int count = 0;
    if(id != null) count = userService.idCheck(id);
    return count;
  }

  //    @PostMapping("/signup/idCheck")
//    @ResponseBody
//    public String idCheck(
//        @RequestBody String filterJSON,
//        HttpServletResponse response){
//        System.out.println("================================================");
//        System.out.println(filterJSON);
//        return null;
//    };



}