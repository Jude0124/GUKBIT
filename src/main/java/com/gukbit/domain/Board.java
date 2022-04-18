package com.gukbit.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Table
public class Board implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @Column
    private String author;

    @Column
    private LocalDateTime date;

    @Column
    private Integer view;

    @Column
    private String title;

    @Column
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
    public Board(Long bid, String author, LocalDateTime date, Integer view, String title, String content, String b_academy_code, String b_course_id, boolean visible, int recommend) {
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
