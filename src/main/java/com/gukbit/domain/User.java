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
    @Column(name = "userIdx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;
    @Column(name = "user_id")
    private String userId;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String tel;
    @Column
    private String nickname;
    @Column(columnDefinition = "integer default 0", insertable=false)
    private Integer auth;
    @Column
    private Integer rights;
    @Lob
    private byte[] image;

    @Builder
    public User(Long userIdx, String userId, String password, String email, String tel) {
        this.userIdx = userIdx;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.tel = tel;
    }
}
