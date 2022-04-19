package com.gukbit.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table
public class AuthUserData {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aid;

    @JoinColumn(name = "user_id")
    private String userId;

    @Column(name = "academy_code")
    private String academyCode;

    @Column(name = "course_id")
    private String courseId;
}
