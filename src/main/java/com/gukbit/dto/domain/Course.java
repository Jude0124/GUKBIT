package com.gukbit.dto.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Set;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Course {
    @Id
    @Column
    private String cid;

    @Column(name = "academy_code")
    private String academycode;

    @Column
    private String id;

    @Column
    private Integer session;

    @Column(name = "field_m")
    private String fieldm;

    @Column(name = "field_s")
    private String fields;

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
        this.academycode = academy_code;
        this.id = id;
        this.session = session;
        this.fieldm = field_m;
        this.fields = field_s;
        this.d_field_ss = d_field_ss;
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
