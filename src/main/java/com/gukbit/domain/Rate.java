package com.gukbit.domain;

import javax.persistence.*;

import lombok.*;


@Getter @Setter
@Entity
@Table(name = "rate")
@Builder
@ToString
@AllArgsConstructor
public class Rate {

  @Id
  @Column
  private String rid;
  @Column
  private String c_cid;
  @Column(name = "user_id")
  private String userId;
  @Column
  private String one_statement;
  @Column
  private Double lecturers_eval;
  @Column
  private Double curriculum_eval;
  @Column
  private Double employment_eval;
  @Column
  private Double culture_eval;
  @Column
  private Double facility_eval;
  @Column
  private String advantage;
  @Column
  private String disadvantage;

  public Rate() {
  }
}
