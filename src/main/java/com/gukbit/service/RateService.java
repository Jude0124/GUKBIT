package com.gukbit.service;

import com.gukbit.domain.Academy;
import com.gukbit.dto.AcademyDto;
import com.gukbit.dto.RateDto;
import com.gukbit.repository.AcademyRepository;
import com.gukbit.repository.RateRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private RateRepository rateRepository;

  @Autowired
  public RateService(RateRepository rateRepository) {
    this.rateRepository = rateRepository;
  }

  @Transactional
  public void saveReview(RateDto rateDto) {
    System.out.println("save review 페이지 "+ rateDto);
    rateRepository.save(rateDto.toEntity());
  }
}
