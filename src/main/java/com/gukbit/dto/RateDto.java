package com.gukbit.dto;

import com.gukbit.domain.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RateDto {
  private String rid;
  private String cCid;
  private String userId;
  private String oneStatement;
  private Double lecturersEval;
  private Double curriculumEval;
  private Double employmentEval;
  private Double cultureEval;
  private Double facilityEval;
  private String advantage;
  private String disadvantage;
  private String date;
  
  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Rate toEntity(){
    Rate build = Rate.builder()
        .rid(rid)
        .cCid(cCid)
        .userId(userId)
        .date(date)
        .oneStatement(oneStatement)
        .lecturersEval(lecturersEval)
        .curriculumEval(curriculumEval)
        .employmentEval(employmentEval)
        .cultureEval(cultureEval)
        .facilityEval(facilityEval)
        .advantage(advantage)
        .disadvantage(disadvantage)
        .build();
    return build;
  }
}
