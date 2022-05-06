package com.gukbit.repository;

import com.gukbit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUserId (String userId);

  User findByEmail(String email);

  User findByTel(String tel);
}

