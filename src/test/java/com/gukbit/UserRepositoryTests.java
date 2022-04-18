package com.gukbit;

import static org.assertj.core.api.Assertions.assertThat;

import com.gukbit.dto.domain.User;
import com.gukbit.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

  @Autowired
  private UserRepository repository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  public void testCreateUser(){
    User user = new User();
    user.setUserId("sam");
    user.setEmail("sam79083@gmail.com");
    user.setPassword("1234");
    user.setTel("010-1234-5678");

    User savedUser = repository.save(user);
    User existUser = entityManager.find(User.class, savedUser.getUser_idx());

    assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
  }
}
