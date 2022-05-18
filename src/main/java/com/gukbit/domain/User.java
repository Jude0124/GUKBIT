package com.gukbit.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@Data
@Entity
@ToString
@Table(name = "user")
@DynamicUpdate
public class User {
    @Id
    @Column(name = "user_idx")
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
    @Column(name = "lock_user")
    private Boolean lockUser;
    @Column
    private String provider;
    @Column(name = "provider_id")
    private String providerId;
    @Column
    private Integer auth;
    //    @Column
//    private Integer rights;
    @Column(name = "profile_image_name")
    private String profileImageName;

    @Builder
    public User(String userId, String password, String email, String tel, String role, String provider, String providerId, Boolean lockUser, Integer auth) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.lockUser = lockUser;
        this.auth = auth;
    }
}
