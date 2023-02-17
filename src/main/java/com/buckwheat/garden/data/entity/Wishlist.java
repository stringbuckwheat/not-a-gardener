package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "wishlist")
public class Wishlist {
    /* 위시 리스트 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int wishlistNo;

    @NotNull
    private String plantName;

    @Column(columnDefinition = "TEXT")
    private String detail;

    // FK
    @ManyToOne
    @JoinColumn(name="member_no")
    private Member member;
}
