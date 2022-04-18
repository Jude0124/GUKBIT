package com.gukbit.repository;

import com.gukbit.domain.Rate;
import com.gukbit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, String> {

}
