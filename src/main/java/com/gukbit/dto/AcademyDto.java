package com.gukbit.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class AcademyDto {

  private String code;
  private String name;
  private String homeUrl;
  private String region;
  private String addr;
  private Double eval;
  private String tel;
  private String imageUrl;
}
