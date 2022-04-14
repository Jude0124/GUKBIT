package com.gukbit.domain;

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
    private String sep;

    @Column
    private String type;

    @Column
    private String start;

    @Column
    private String end;

    @Builder
    public Course(String academy_code, String id, Integer session, String field_m, String field_s, String sep, String type, String start, String end) {
        this.cid = id+session;
        this.academy_code = academy_code;
        this.id = id;
        this.session = session;
        this.field_m = field_m;
        this.field_s = field_s;
        this.sep = sep;
        this.type = type;
        this.start = start;
        this.end = end;
    }
}
