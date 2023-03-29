package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "plant_status")
public class PlantStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int plantStatusNo;

    @NotNull
    private LocalDate updateDate;

    // FK
    @OneToOne
    @JoinColumn(name = "status_no")
    private PlantStatusInfo plantStatusInfo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "plant_no")
    private Plant plant;
}
