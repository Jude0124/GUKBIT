package com.gukbit.repository;

import com.gukbit.domain.Academy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {

   Academy findByCode(String code);

  @Query(value = "SELECT a FROM Academy a WHERE a.name LIKE %:keyword% ORDER BY a.name")
  List<Academy> findByNameContaining(@Param("keyword") String keyword);

  @Transactional
  @Modifying
  @Query(nativeQuery=true, value ="UPDATE academy a set a.eval =:eval where a.code=:code")
    int setEval(@Param("code") String code, @Param("eval") double eval);
}
