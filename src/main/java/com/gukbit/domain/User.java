package com.gukbit.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

  @Id
  @Column(name = "user_idx")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long user_idx;
  @Column(name = "user_id")
  private String userId;
  @Column
  private String password;
  @Column
  private String email;
  @Column
  private String tel;

  @Builder
  public User(Long user_idx, String userId, String password, String email, String tel) {
    this.user_idx = user_idx;
    this.userId = userId;
    this.password = password;
    this.email = email;
    this.tel = tel;
  }
}
