package com.gukbit.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Entity
@Table(name = "rate")
@Builder
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
