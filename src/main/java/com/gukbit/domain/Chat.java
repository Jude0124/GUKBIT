package com.gukbit.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Chat {

    @Id
    @Column(name = "chat_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatIdx;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "academy_code")
    private String academyCode;

    @Column(name = "chat_content")
    private String chatContent;

    @Column(name = "chat_date")
    private String chatDate;
}
