package com.gukbit.service;

import com.gukbit.dto.domain.Academy;
import com.gukbit.dto.AcademyDto;
import com.gukbit.service.repository.AcademyRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AcademyService {

  private AcademyRepository academyRepository;

  public AcademyService(AcademyRepository academyRepository) {
    this.academyRepository = academyRepository;
  }

  @Transactional
  public List<AcademyDto> searchAcademy(String keyword) {
    List<Academy> academies = academyRepository.findByNameContaining(keyword);
    List<AcademyDto> academyDtoList = new ArrayList<>();

    if(academies.isEmpty()) return academyDtoList;
    for(Academy academy : academies){
      academyDtoList.add(this.convertEntityToDto(academy));
    }
    return academyDtoList;
  }

  private AcademyDto convertEntityToDto(Academy academy){
    return AcademyDto.builder()
        .code(academy.getCode())
        .name(academy.getName())
        .home_url(academy.getHome_url())
        .region(academy.getRegion())
        .addr(academy.getAddr())
        .eval(academy.getEval())
        .tel(academy.getTel())
        .build();
  }

}
