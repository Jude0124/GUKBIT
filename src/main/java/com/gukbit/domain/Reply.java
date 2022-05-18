package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@ToString
@Builder
public class Reply {
    @Id
    @Column(name = "rid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;

    @Column(name = "r_bid")
    private Integer rBid;

    @Column(name = "r_rid")
    private Integer rRid;

    @Column(name = "r_author")
    private String rAuthor;

    @Column(name = "r_content")
    private String rContent;

    @Column(name = "r_date")
    private String rDate;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "r_bid", referencedColumnName="bid", insertable = false, updatable = false)
    private Board board;
}
