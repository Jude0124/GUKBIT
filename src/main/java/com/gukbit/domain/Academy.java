package com.gukbit.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Academy {
    @Id
    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String home_url;

    @Column
    private String region;

    @Column
    private String addr;

    @Column
    private Double eval;

    @Column
    private String tel;

    @Column(name="image_url")
    private String imageUrl;

    @Builder
    public Academy(String code, String name, String home_url, String region, String addr, double eval, String tel, String imageUrl) {
        this.code = code;
        this.name = name;
        this.home_url = home_url;
        this.region = region;
        this.addr = addr;
        this.eval = eval;
        this.tel = tel;
        this.imageUrl = imageUrl;
    }
}
