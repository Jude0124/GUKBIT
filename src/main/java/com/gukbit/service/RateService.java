package com.gukbit.service;

import com.gukbit.dto.RateDto;
import com.gukbit.repository.RateRepository;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private RateRepository rateRepository;

  public RateService(RateRepository rateRepository) {
    this.rateRepository = rateRepository;
  }

  @Transactional
  public void saveReview(RateDto rateDto) {
    System.out.println("save review 페이지 "+ rateDto);
    rateRepository.save(rateDto.toEntity());
  }
}
