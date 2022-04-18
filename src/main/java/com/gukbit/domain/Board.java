package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
@ToString
public class Board implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @Column(columnDefinition = "varchar(45) not null comment '등록자'")
    private String author;

    @Column
    private String date;

    @Column
    private Integer view;

    @Column(columnDefinition = "varchar(30) not null comment '타이틀'")
    private String title;

    @Column(columnDefinition = "varchar(500) not null comment '내용'")
    private String content;

    @Column
    private String b_academy_code;

    @Column
    private String b_course_id;

    @Column
    private boolean visible;

    @Column
    private int recommend;

    @Builder

    public Board(Long bid, String author, String date, Integer view, String title, String content, String b_academy_code, String b_course_id, boolean visible, int recommend) {
        this.bid = bid;
        this.author = author;
        this.date = date;
        this.view = view;
        this.title = title;
        this.content = content;
        this.b_academy_code = b_academy_code;
        this.b_course_id = b_course_id;
        this.visible = visible;
        this.recommend = recommend;
    }
}
