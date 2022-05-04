package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Data
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
//    @Column
//    private String nickname;
    @Column
    private String role;
    @Column
    private Boolean lock;
    @Column
    private String provider;
    @Column(name = "provider_id")
    private String providerId;
    @Column
    private Integer auth;
//    @Column
//    private Integer rights;
    @Lob
    private byte[] image;

    @Builder
    public User(String userId, String password, String email, String tel, String role,String provider,String providerId,Boolean lock) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.role = role;
        this.provider =provider;
        this.providerId = providerId;
        this.lock = lock;
    }
}
