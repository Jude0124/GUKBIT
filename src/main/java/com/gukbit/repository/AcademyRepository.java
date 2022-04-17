package com.gukbit.repository;

import com.gukbit.domain.Academy;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, String> {
  List<Academy> findByNameContaining(String keyword);
}
