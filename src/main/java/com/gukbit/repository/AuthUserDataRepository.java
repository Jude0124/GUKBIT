package com.gukbit.repository;

import com.gukbit.domain.AuthUserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserDataRepository extends JpaRepository<AuthUserData,Integer> {
    AuthUserData findByUserId(String userId);
}
