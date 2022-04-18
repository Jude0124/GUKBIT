package com.gukbit.service.repository;

import com.gukbit.dto.domain.Academy;

<<<<<<< HEAD:src/main/java/com/gukbit/service/repository/AcademyRepository.java
=======
import com.gukbit.domain.Academy;
import com.gukbit.domain.Course;
import com.gukbit.service.AcademyService;
>>>>>>> f65fae4faf245fac58157e6c2549abfe1aab7272:src/main/java/com/gukbit/repository/AcademyRepository.java
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {

   Academy findByCode(String code);
  @Query(value = "SELECT a FROM Academy a WHERE a.name LIKE %:keyword% ORDER BY a.name")
  List<Academy> findByNameContaining(@Param("keyword") String keyword);

}
