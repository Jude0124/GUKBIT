package com.gukbit.repository;

import com.gukbit.domain.Academy;
import com.gukbit.domain.AuthUserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthUserDataRepository extends JpaRepository<AuthUserData,Integer> {
    AuthUserData findByUserId(String userId);

    @Query(value = "SELECT c.academyCode FROM AuthUserData c where c.userId=:userId")
    String findAcademyCodeByUserId(@Param("userId") String userId);
}
