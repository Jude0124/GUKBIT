package com.gukbit.dto.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Course {
    @Id
    @Column
    private String cid;

    @Column
    private String academy_code;

    @Column
    private String id;

    @Column
    private Integer session;

    @Column
    private String field_m;

    @Column
    private String field_s;

    @Column
    private String start;

    @Column
    private String end;

    @Column
    private String d_field_ss;

    @Column
    private String name;

    @Builder
    public Course(String academy_code, String id, Integer session, String field_m, String field_s, String d_field_ss, String start, String end, String name) {
        this.cid = id+session;
        this.academy_code = academy_code;
        this.id = id;
        this.session = session;
        this.field_m = field_m;
        this.field_s = field_s;
        this.d_field_ss = d_field_ss;
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
