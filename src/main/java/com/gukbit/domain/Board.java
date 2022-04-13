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
    private Integer no;

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
    private String academyCode;

    @Column
    private String courseId;

    @Column
    private boolean visible;

    @Column
    private int recommend;

    @Builder
    public Board(Integer no, String author, LocalDateTime date, Integer view, String title, String content, String academyCode, String courseId, boolean visible, int recommend) {
        this.no = no;
        this.author = author;
        this.date = date;
        this.view = view;
        this.title = title;
        this.content = content;
        this.academyCode = academyCode;
        this.courseId = courseId;
        this.visible = visible;
        this.recommend = recommend;
    }
}
