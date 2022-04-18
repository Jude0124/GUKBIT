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
  private String c_cid;
  private String user_id;
  private String one_statement;
  private Long lecturers_eval;
  private Long curriculum_eval;
  private Long employment_eval;
  private Long culture_eval;
  private Long facility_eval;
  private String advantage;
  private String disadvantage;
  
  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Rate toEntity(){
    Rate build = Rate.builder()
        .rid(rid)
        .c_cid(c_cid)
        .user_id(user_id)
        .one_statement(one_statement)
        .lecturers_eval(lecturers_eval)
        .curriculum_eval(curriculum_eval)
        .employment_eval(employment_eval)
        .culture_eval(culture_eval)
        .facility_eval(facility_eval)
        .advantage(advantage)
        .disadvantage(disadvantage)
        .build();
    return build;
  }
}
