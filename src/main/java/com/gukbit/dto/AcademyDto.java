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

  /*@Builder
  public AcademyDto(String code, String name, String homeUrl, String region,
      String addr, double eval, String tel){
    this.code=code;
    this.name=name;
    this.homeUrl=homeUrl;
    this.region=region;
    this.addr=addr;
    this.eval=eval;
    this.tel=tel;
  }*/
}
