package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Division_S {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String d_field_s;


    @Column
    private String div;





}
