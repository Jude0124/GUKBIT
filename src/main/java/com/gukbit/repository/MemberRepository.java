package com.gukbit.repository;

import com.gukbit.model.MemberModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository {
  public int setInsert(MemberModel memberVO) throws Exception;
}
