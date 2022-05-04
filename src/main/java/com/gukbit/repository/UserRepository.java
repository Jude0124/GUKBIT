package com.gukbit.repository;

import com.gukbit.domain.Academy;
import com.gukbit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUserId (String userId);

  @Query(value = "SELECT user FROM User user WHERE user.userId LIKE %:userId% ORDER BY user.userId")
  List<User> findByUserIdContaining(@Param("userId") String userId);
}

