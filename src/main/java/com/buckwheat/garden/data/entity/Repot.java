package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table
public class Repot {
    /* 분갈이 날짜 기록 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int repotNo;

    @NotNull
    private LocalDate repotDate;

    // FK
    @ManyToOne
    @JoinColumn(name = "plant_no")
    private Plant plant;
}
