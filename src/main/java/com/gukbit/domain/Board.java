package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@ToString
public class Board{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bid;

    @Column(columnDefinition = "varchar(45) not null comment '등록자'")
    private String author;

    @Column
    private String date;

    @Column(columnDefinition = "integer default 0", nullable = false, insertable=false)
    private Integer view;

    @Column(columnDefinition = "TEXT not null comment '타이틀'", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT not null comment '내용'")
    private String content;

    @Column(name = "bAcademyName")
    private String bAcademyName;

    @Column
    private String b_course_code;

    @Column
    private String b_course_name;

    @Column
    private String b_course_id;

    @Column (insertable=false)
    private Boolean visible;

    @Column
    private Integer recommend;

    @Column(name = "b_academy_code")
    private String bacademycode;


    @Builder
    public Board(Integer bid, String author, String date, Integer view, String title, String content,
        String b_academy_code, String b_course_code, String bAcademyName, String b_course_id,
        boolean visible, int recommend, String b_course_name) {
        this.bid = bid;
        this.author = author;
        this.date = date;
        this.view = view;
        this.title = title;
        this.content = content;
        this.bacademycode = b_academy_code;
        this.b_course_id = b_course_id;
        this.bAcademyName = bAcademyName;
        this.b_course_code = b_course_code;
        this.visible = visible;
        this.recommend = recommend;
        this.b_course_name = b_course_name;
    }
}
