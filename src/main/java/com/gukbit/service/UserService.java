package com.gukbit.service;


import com.gukbit.dto.domain.User;
import com.gukbit.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void joinUser(User user) {
    userRepository.save(user);
  }

  public int idCheck(String id) {
    User user = userRepository.findByUserId(id);
    if(user==null) return 0;
    if(user.getUserId().equals(id)){
      return 1;
    }
    return 0;
  }
}
