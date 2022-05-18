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

    @Column (name= "home_url")
    private String homeUrl;

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
    public Academy(String code, String name, String homeUrl, String region, String addr, Double eval, String tel, String imageUrl) {
        this.code = code;
        this.name = name;
        this.homeUrl = homeUrl;
        this.region = region;
        this.addr = addr;
        this.eval = eval;
        this.tel = tel;
        this.imageUrl = imageUrl;
    }
}
