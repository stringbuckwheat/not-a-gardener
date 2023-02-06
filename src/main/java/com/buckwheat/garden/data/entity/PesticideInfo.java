package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pesticide_info")
public class PesticideInfo {
    /* 농약 정보 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pesticideInfoNo;

    @NotNull
    private String pesticideName;

    @NotNull
    private int pesticidePeriod;

    // FK
    @ManyToOne
    @JoinColumn(name="username")
    private Member member;
}
