package com.gukbit.repository;

import com.gukbit.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
  @Modifying
  @Query("update Notice a set a.view = a.view + 1 where a.bid = :id")
  int updateView(@Param("id") int id);
}
