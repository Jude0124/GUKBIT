package com.gukbit.repository;

import com.gukbit.domain.Academy;
import com.gukbit.service.AcademyService;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {
  @Query(value = "SELECT a FROM Academy a WHERE a.name LIKE %:keyword%")
  List<Academy> findByNameContaining(@Param("keyword") String keyword);
}
