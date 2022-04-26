package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "rate")
@Builder
@ToString
@AllArgsConstructor
public class Rate {
    @Id
    @Column(name = "rid")
    private String rid;
    @Column(name = "c_cid")
    private String cCid;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "one_statement")
    private String oneStatement;
    @Column(name = "lecturers_eval")
    private Double lecturersEval;
    @Column(name = "curriculum_eval")
    private Double curriculumEval;
    @Column(name = "employment_eval")
    private Double employmentEval;
    @Column(name = "culture_eval")
    private Double cultureEval;
    @Column(name = "facility_eval")
    private Double facilityEval;
    @Column(name = "advantage")
    private String advantage;
    @Column(name = "disadvantage")
    private String disadvantage;
    @Column(name = "date")
    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "c_cid", referencedColumnName = "cid", insertable = false, updatable = false)
    private Course course;

    public Rate() {
    }
}
