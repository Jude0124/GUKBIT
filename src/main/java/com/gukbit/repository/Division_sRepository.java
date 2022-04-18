package com.gukbit.repository;

import com.gukbit.domain.Course;
import com.gukbit.domain.Division_S;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface Division_sRepository extends JpaRepository<Division_S, Long> {

}

