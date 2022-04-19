package com.gukbit.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@Table(name = "auth_user_data")
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

    @Column(name = "session")
    private Integer session;
}
