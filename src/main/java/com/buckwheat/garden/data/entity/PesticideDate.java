package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pesticide_date")
public class PesticideDate {
    /* 농약, 살균, 살충제 관주 및 도포 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pesticideDateNo;

    @NotNull
    private LocalDate pesticideDate;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plant_no")
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesticide_no")
    private Pesticide pesticideInfo;

}
