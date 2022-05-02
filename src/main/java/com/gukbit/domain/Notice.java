package com.gukbit.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

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

    @Column(columnDefinition = "TEXT not null comment '타이틀'")
    private String title;

    @Column(columnDefinition = "TEXT not null comment '내용'")
    private String content;


}
