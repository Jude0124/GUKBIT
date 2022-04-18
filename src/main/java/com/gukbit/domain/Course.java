package com.gukbit.domain;

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
        this.academycode = academy_code;
        this.id = id;
        this.session = session;
        this.fieldm = field_m;
        this.fields = field_s;
        this.sep = sep;
        this.type = type;
        this.start = start;
        this.end = end;
    }
}
