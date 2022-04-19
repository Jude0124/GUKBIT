package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
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
    @Column
    String nickname;
    @Column
    String tell;
    @Column
    Integer auth;
    @Column
    Integer rights;
    @Lob
    byte[] image;

  @Builder
  public User(Long user_idx, String userId, String password, String email, String tel) {
    this.user_idx = user_idx;
    this.userId = userId;
    this.password = password;
    this.email = email;
    this.tel = tel;
  }
}
