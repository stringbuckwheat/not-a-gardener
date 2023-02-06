package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "place")
public class Place {
    /* 식물을 놓은 장소 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int placeNo;

    @NotNull
    private String placeName;

    private String artificialLight;

    private String outside;

    // FK
    @ManyToOne
    @JoinColumn(name="username")
    private Member member;
}
