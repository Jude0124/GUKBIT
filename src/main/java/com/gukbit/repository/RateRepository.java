
package com.gukbit.repository;

import com.gukbit.domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, String> {

  Rate findByUserId(String userId);

  void deleteByUserId(String userId);

  Rate findByRid(String rid);

  List<Rate> findAllBycCidIn(List<String> courseId);
}

