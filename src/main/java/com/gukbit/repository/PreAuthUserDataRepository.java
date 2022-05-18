package com.gukbit.repository;

import com.gukbit.domain.AuthUserData;
import com.gukbit.domain.PreAuthUserData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreAuthUserDataRepository extends JpaRepository<PreAuthUserData,Integer> {

  PreAuthUserData findByUserId(String userId);

  List<PreAuthUserData> findAllByUserIdContaining(String UserId);
}
