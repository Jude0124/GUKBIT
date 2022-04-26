package com.gukbit.repository;

import com.gukbit.domain.DivisionS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionSRepository extends JpaRepository<DivisionS, Long> {

}

